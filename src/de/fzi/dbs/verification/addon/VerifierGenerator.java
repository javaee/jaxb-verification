package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JPrimitiveType;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.XSDatatype;
import com.sun.msv.grammar.Expression;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.FieldUse;
import com.sun.tools.xjc.grammar.PrimitiveItem;
import com.sun.tools.xjc.grammar.TypeItem;
import de.fzi.dbs.verification.ObjectVerifier;
import de.fzi.dbs.verification.addon.datatype.VerificatorConstructor;
import de.fzi.dbs.verification.addon.datatype.VerificatorConstructorFactory;
import de.fzi.dbs.verification.event.EntryLocator;
import de.fzi.dbs.verification.event.VerificationEvent;
import de.fzi.dbs.verification.event.VerificationEventLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.ValidationEventHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Editor generator.
 *
 * @author Aleksei Valikov
 */
public class VerifierGenerator extends ClassBasedGenerator
{
  /**
   * Logger.
   */
  protected Log log = LogFactory.getLog(VerifierGenerator.class);

  /**
   * Expression of the class that is being processed.
   */
  protected Expression expression;

  /**
   * Check method.
   */
  protected JMethod check;

  /**
   * Check method for object.
   */
  protected JMethod objectCheck;

  /**
   * Check method for root object (withour parent locator).
   */
  protected JMethod rootObjectCheck;

  /**
   * Map of field check methods.
   */
  protected Map fieldChecks;

  /**
   * Constructs a new editor generator.
   *
   * @param classContext class context.
   */
  public VerifierGenerator(final ClassContext classContext)
  {
    super(classContext);
    expression = classContext.target.exp;
  }

  protected JDefinedClass generateClassInternal() throws JClassAlreadyExistsException
  {
    final JDefinedClass objectVerifierClass = getOrGenerateObjectVerifierClass(classContext.ref);

    if (classContext.target.getSuperClass() == null)
    {
      objectVerifierClass._implements(ObjectVerifier.class);
    }
    else
    {
      final JDefinedClass parentPropertyEditorClass =
        getOrGenerateObjectVerifierClass(classContext.parent.getClassContext(classContext.target.getSuperClass()).ref);
      objectVerifierClass._extends(parentPropertyEditorClass);
    }

    return objectVerifierClass;
  }

  protected void generateFields()
  {
  }

  protected void generateMethods()
  {
    // Main methods
    check = generateCheck();
    fieldChecks = generateFieldChecks();

    // Additional methods
    objectCheck = generateObjectCheck();
    rootObjectCheck = generateRootObjectCheck();
  }

  /**
   * Generates the check method.
   *
   * @return Generated method.
   */
  protected JMethod generateCheck()
  {
    final JMethod check = theClass.method(JMod.PUBLIC, codeModel.VOID, "check");
    final JVar parentLocator = check.param(codeModel.ref(VerificationEventLocator.class), "parentLocator");
    final JVar handler = check.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar master = check.param(classContext.ref, "master");
    final JStatement statement = (JStatement) expression.visit(new CheckVisitor(classContext, handler, parentLocator, master));
    check.body().add(statement);
    return check;
  }


  /**
   * Generates field check methods.
   *
   * @return Map of generated methods (field name/check method).
   */
  protected Map generateFieldChecks()
  {
    final Map fieldChecks = new HashMap();
    final FieldUse[] fieldUses = classContext.target.getDeclaredFieldUses();
    for (int index = 0; index < fieldUses.length; index++)
    {
      final FieldUse fieldUse = fieldUses[index];
      final JMethod fieldCheck = generateFieldCheck(fieldUse);
      fieldChecks.put(fieldUse.name, fieldCheck);
    }
    return fieldChecks;
  }

  /**
   * Generates check method for the field.
   *
   * @param fieldUse thefield.
   * @return Generated method.
   */
  protected JMethod generateFieldCheck(final FieldUse fieldUse)
  {
    /// check<Name>(parentLocator, handler, master, value)
    final JMethod fieldCheck = theClass.method(JMod.PUBLIC, codeModel.VOID, "check" + fieldUse.name);
    final JVar parentLocator = fieldCheck.param(codeModel.ref(VerificationEventLocator.class), "parentLocator");
    final JVar handler = fieldCheck.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar master = fieldCheck.param(classContext.ref, "master");

    final JType fieldParamType = fieldUse.multiplicity.isAtMostOnce() ?
      objectType(fieldUse.type)
      : codeModel.ref(List.class);
    final String fieldParamName = fieldUse.multiplicity.isAtMostOnce() ? "value" : "values";
    final JVar value = fieldCheck.param(fieldParamType, fieldParamName);

    final JExpression locator = JExpr._new(classContext.parent.getCodeModel().ref(VerificationEventLocator.class)).arg(parentLocator).arg(master).arg(JExpr.lit(fieldUse.name));

    final JStatement statement = fieldUse.multiplicity.isAtMostOnce() ?
      generateSingleValueCheck(fieldUse, locator, handler, master, value, fieldParamType) :
      generateCollectionValueCheck(fieldUse, parentLocator, handler, master, value);

    fieldCheck.body().add(statement);

    return fieldCheck;
  }

  /**
   * Generates check for a single value.
   *
   * @param fieldUse the field.
   * @param locator  locator.
   * @param handler  handler.
   * @param master   master object.
   * @param value    field value.
   * @param type     value type.
   * @return Statement that checks a single value.
   */
  protected JStatement generateSingleValueCheck(final FieldUse fieldUse, final JExpression locator, final JExpression handler,
    final JExpression master, final JExpression value, final JType type)
  {
    final JBlock block = JBlock.dummyInstance.block();

    // Iterate over type items of the field
    final TypeItem[] typeItems = (TypeItem[]) de.fzi.dbs.jaxb.addon.Util.getTypeItems(fieldUse).toArray(new TypeItem[0]);
    TypeItem.sort(typeItems);
    JBlock currentBlock = block;
    for (int index = typeItems.length - 1; index >= 0; index--)
    {
      final TypeItem typeItem = typeItems[index];
      final JConditional _if = currentBlock._if(valueIsInstanceofTypeItem(value, type, typeItem));
      final JType itemType = objectType(typeItem.getType());
      final JVar realValue = _if._then().decl(itemType, "realValue", JExpr.cast(itemType, value));
      _if._then().add(generateValueCheck(fieldUse, typeItem, locator, handler, master, realValue, itemType));
      currentBlock = _if._else();
    }

    final JConditional ifNull = currentBlock._if(JOp.eq(JExpr._null(), value));
    // todo: null type for nillable values
    ifNull._then().directStatement("// todo: report null");
    ifNull._else().directStatement("// Report wrong class");
    ifNull._else().invoke(handler, "handleEvent").
      arg(JExpr._new(codeModel.ref(VerificationEvent.class)).arg(locator).
      arg(JExpr._new(codeModel.ref(de.fzi.dbs.verification.event.structure.NonExpectedClassProblem.class)).arg(value.invoke("getClass"))));
    return block;
  }

  /**
   * Generates collection value check.
   *
   * @param fieldUse      the field.
   * @param parentLocator parentLocator.
   * @param handler       handler.
   * @param master        master.
   * @param values        the collection.
   * @return Statement for collection check.
   */
  protected JStatement generateCollectionValueCheck(final FieldUse fieldUse, final JExpression parentLocator, final JExpression handler, final JExpression master, final JExpression values)
  {
    final JMethod entryCheck = generateCollectionValueEntryCheck(fieldUse);

    final JBlock block = JBlock.dummyInstance.block();
    final JForLoop _for = block._for();
    final JVar index = _for.init(codeModel.INT, "index", JExpr.lit(0));
    _for.test(JOp.lt(index, values.invoke("size")));
    _for.update(JOp.incr(index));
    final JType objectClass = codeModel.ref(Object.class);
    final JVar item = _for.body().decl(objectClass, "item", JExpr.invoke(values, "get").arg(index));
    _for.body().invoke(entryCheck).arg(parentLocator).arg(handler).arg(master).arg(index).arg(item);
    return block;
  }

  /**
   * Generates check method for the collection entry.
   * @param fieldUse field use.
   * @return Generated method.
   */
  protected JMethod generateCollectionValueEntryCheck(final FieldUse fieldUse)
  {
    final JType objectClass = codeModel.ref(Object.class);
    /// check<Name>(locator, handler, master, value)
    final JMethod check = theClass.method(JMod.PUBLIC, codeModel.VOID, "check" + fieldUse.name);
    final JVar parentLocator = check.param(codeModel.ref(VerificationEventLocator.class), "parentLocator");
    final JVar handler = check.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar master = check.param(classContext.ref, "master");
    final JVar index = check.param(codeModel.INT, "index");
    final JVar value = check.param(objectClass, "value");
    final JExpression entryLocator = JExpr._new(codeModel.ref(EntryLocator.class)).arg(parentLocator).arg(master).arg(JExpr.lit(fieldUse.name)).arg(index);
    check.body().add(generateSingleValueCheck(fieldUse, entryLocator, handler, master, value, objectClass));
    return check;
  }

  /**
   * Generates value checking statement.
   *
   * @param fieldUse the field.
   * @param typeItem the type.
   * @param locator  locator.
   * @param handler  handler.
   * @param master   master object.
   * @param value    value.
   * @param type     value type.
   * @return Value check statement.
   */
  protected JStatement generateValueCheck(final FieldUse fieldUse, final TypeItem typeItem, final JExpression locator, final JExpression handler, final JExpression master, final JExpression value, final JType type)
  {
    final JBlock block = JBlock.dummyInstance.block();
    if (typeItem instanceof PrimitiveItem)
    {
      block.directStatement("// Check primitive value");
      block.add(generatePrimitiveValueCheck(fieldUse, (PrimitiveItem) typeItem, locator, handler, master, value));
    }
    else if (typeItem instanceof ClassItem)
    {
      block.directStatement("// Check complex value");
      final JDefinedClass valueClass = (JDefinedClass) type;
      final JDefinedClass verifierClass = getOrGenerateObjectVerifierClass(valueClass);
      final JVar verifier = block.decl(verifierClass, "verifier", JExpr._new(verifierClass));
      block.invoke(verifier, "check").arg(locator).arg(handler).arg(value);
    }
    else
    {
      block.directStatement("// Type item " + typeItem.getClass() + " is not supported");
    }
    return block;
  }

  /**
   * Generates checking statement for primitive value.
   *
   * @param fieldUse the field.
   * @param item     the item.
   * @param locator  locator.
   * @param handler  handler.
   * @param master   master object.
   * @param value    value.
   * @return Check statement for the primitive value.
   */
  protected JStatement generatePrimitiveValueCheck(final FieldUse fieldUse, final PrimitiveItem item, final JExpression locator, final JExpression handler, final JExpression master, final JExpression value)
  {
    final JBlock block = JBlock.dummyInstance.block();
    final DatabindableDatatype guard = item.guard;
    if (guard instanceof XSDatatype && ((XSDatatype) guard).isAlwaysValid())
    {
      block.directStatement("// Primitive value is always valid, nothing to check");
    }
    else
    {
      block.directStatement("// Perform the check");
      final VerificatorConstructor vc = VerificatorConstructorFactory.getVerificatorConstructor(guard);
      if (null == vc)
      {
        block.directStatement("// VerificatorConstructor for " + guard.getClass() + " is not implemented.");
      }
      else
      {
        block.directStatement("// Checking " + guard.getClass() + " datatype");
        final JVar problem = block.decl(codeModel.ref(de.fzi.dbs.verification.event.datatype.ValueProblem.class), "problem", JExpr._null());
        block.add(vc.verify(guard, codeModel, theClass, value, problem));
        final JConditional ifProblemIsNotNull = block._if(JOp.ne(JExpr._null(), problem));
        ifProblemIsNotNull._then().directStatement("// Handle event");
        ifProblemIsNotNull._then().invoke(handler, "handleEvent").arg(JExpr._new(codeModel.ref(VerificationEvent.class)).arg(locator).arg(problem));
      }
    }
    return block;
  }

  /**
   * Generates object check method.
   *
   * @return Object check method.
   */
  protected JMethod generateObjectCheck()
  {
    final JMethod objectCheck = theClass.method(JMod.PUBLIC, codeModel.VOID, "check");
    final JVar parentLocator = objectCheck.param(codeModel.ref(VerificationEventLocator.class), "parentLocator");
    final JVar handler = objectCheck.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar object = objectCheck.param(Object.class, "object");
    objectCheck.body().invoke(this.check).arg(parentLocator).arg(handler).arg(JExpr.cast(classContext.ref, object));
    return objectCheck;
  }

  /**
   * Generates root object check method.
   *
   * @return Root object check method.
   */
  protected JMethod generateRootObjectCheck()
  {
    final JMethod rootObjectCheck = theClass.method(JMod.PUBLIC, codeModel.VOID, "check");
    final JVar handler = rootObjectCheck.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar object = rootObjectCheck.param(Object.class, "object");
    rootObjectCheck.body().invoke(this.objectCheck).arg(JExpr._null()).arg(handler).arg(JExpr.cast(classContext.ref, object));
    return rootObjectCheck;
  }


  /**
   * Generates new or returns existing object verifier class for given class.
   *
   * @param theClass the class to be verified.
   * @return Object verifier class for given class.
   */
  public static JDefinedClass getOrGenerateObjectVerifierClass(final JDefinedClass theClass)
  {
    final JClassContainer container = theClass.outer() instanceof JDefinedClass ?
      (JClassContainer) getOrGenerateObjectVerifierClass((JDefinedClass) theClass.outer()) :
      theClass._package().subPackage("verification");

    final int modifiers = theClass.outer() instanceof JDefinedClass ?
      (JMod.PUBLIC | JMod.STATIC) : JMod.PUBLIC;

    final String name = theClass.name() + "Verifier";

    try
    {
      return container._class(modifiers, name);
    }
    catch (JClassAlreadyExistsException jcaeex)
    {
      return jcaeex.getExistingClass();
    }
  }

  /**
   * Generates <code><em>value</em> instanceof <em>type</em></code> expression.
   *
   * @param value    the value.
   * @param type     type of the value.
   * @param typeItem type item.
   * @return Generated expression.
   */
  public static JExpression valueIsInstanceofTypeItem(final JExpression value, final JType type, final TypeItem typeItem)
  {
    final JType itemType = typeItem.getType();
    // For the primitive types, check types statically
    if (type.isPrimitive())
    {
      if (type.equals(itemType))
      {
        return JExpr.TRUE;
      }
      else
      {
        return JExpr.FALSE;
      }
    }
    else
    {
      return JOp._instanceof(value, objectType(typeItem.getType()));
    }
  }

  /**
   * Returns object type for the given type. This method will return wrapper classes for primitive types.
   *
   * @param type the type.
   * @return Object type for given type.
   */
  public static JType objectType(final JType type)
  {
    return type.isPrimitive() ? ((JPrimitiveType) type).getWrapperClass() : type;

  }
}
