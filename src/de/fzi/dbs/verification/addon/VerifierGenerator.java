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
import com.sun.msv.grammar.AttributeExp;
import com.sun.msv.grammar.ChoiceExp;
import com.sun.msv.grammar.ConcurExp;
import com.sun.msv.grammar.DataExp;
import com.sun.msv.grammar.ElementExp;
import com.sun.msv.grammar.Expression;
import com.sun.msv.grammar.ExpressionPool;
import com.sun.msv.grammar.InterleaveExp;
import com.sun.msv.grammar.ListExp;
import com.sun.msv.grammar.MixedExp;
import com.sun.msv.grammar.OneOrMoreExp;
import com.sun.msv.grammar.OtherExp;
import com.sun.msv.grammar.ReferenceExp;
import com.sun.msv.grammar.SequenceExp;
import com.sun.msv.grammar.ValueExp;
import com.sun.msv.grammar.xmlschema.OccurrenceExp;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.ExternalItem;
import com.sun.tools.xjc.grammar.FieldItem;
import com.sun.tools.xjc.grammar.FieldUse;
import com.sun.tools.xjc.grammar.IgnoreItem;
import com.sun.tools.xjc.grammar.InterfaceItem;
import com.sun.tools.xjc.grammar.PrimitiveItem;
import com.sun.tools.xjc.grammar.SuperClassItem;
import com.sun.tools.xjc.grammar.TypeItem;
import de.fzi.dbs.verification.addon.util.Util;
import de.fzi.dbs.verification.datatype.VerificatorConstructor;
import de.fzi.dbs.verification.datatype.VerificatorConstructorFactory;
import de.fzi.dbs.verification.datatype.problem.Problem;
import de.fzi.dbs.verification.event.DatatypeEvent;
import de.fzi.dbs.verification.event.EntryLocator;
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
  protected Expression expression;
  protected ExpressionPool expressionPool;

  protected JMethod check;
  protected Map fieldChecks;

  /**
   * Constructs a new editor generator.
   *
   * @param classContext class context.
   */
  public VerifierGenerator(final ClassContext classContext)
  {
    super(classContext);
    expressionPool = classContext.parent.getGrammar().getPool();
    expression = classContext.target.exp;
  }

  protected JDefinedClass generateClassInternal() throws JClassAlreadyExistsException
  {
    final JDefinedClass propertyEditorClass = getOrGenerateEditorClass(classContext.ref);

    if (classContext.target.getSuperClass() == null)
    {
    }
    else
    {
      final JDefinedClass parentPropertyEditorClass =
        getOrGenerateEditorClass(classContext.parent.getClassContext(classContext.target.getSuperClass()).ref);
      propertyEditorClass._extends(parentPropertyEditorClass);
    }

    return propertyEditorClass;
  }

  protected void generateFields()
  {
  }

  protected void generateMethods()
  {
    check = generateCheck();
    fieldChecks = generateFieldChecks();
  }

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

  protected JMethod generateFieldCheck(final FieldUse fieldUse)
  {
    final JMethod fieldCheck = theClass.method(JMod.PUBLIC, codeModel.VOID, "check" + fieldUse.name);
    final JVar parentLocator = fieldCheck.param(codeModel.ref(VerificationEventLocator.class), "parentLocator");
    final JVar handler = fieldCheck.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar master = fieldCheck.param(classContext.ref, "master");
    final JType fieldParamType = fieldUse.multiplicity.isAtMostOnce() ?
      (fieldUse.type.isPrimitive() ? ((JPrimitiveType) fieldUse.type).getWrapperClass() : fieldUse.type)
      : codeModel.ref(List.class);
    final String fieldParamName = fieldUse.multiplicity.isAtMostOnce() ? "value" : "values";
    final JVar value = fieldCheck.param(fieldParamType, fieldParamName);
    if (fieldUse.multiplicity.isAtMostOnce())
    {
      fieldCheck.body().add(generateSingleFieldCheck(fieldUse, handler, parentLocator, master, value));
    }
    else
    {
      fieldCheck.body().add(generateCollectionFieldCheck(fieldUse, handler, parentLocator, master, value));
    }
    return fieldCheck;
  }

  protected JExpression instanceofTypeItem(final JVar value, final TypeItem typeItem)
  {
    final JType valueType = value.type();
    final JType itemType = typeItem.getType();
    // For the primitive types, check types statically
    if (valueType.isPrimitive())
    {
      if (valueType.equals(itemType))
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
      return JOp._instanceof(value, typeItem.getType().isPrimitive() ? ((JPrimitiveType) typeItem.getType()).getWrapperClass() : typeItem.getType());
    }
  }

  protected JStatement generateSingleFieldCheck(final FieldUse fieldUse, final JVar handler, final JExpression locator, final JVar master, final JVar value)
  {
    final JBlock block = JBlock.dummyInstance.block();
    final TypeItem[] typeItems = (TypeItem[]) de.fzi.dbs.jaxb.addon.Util.getTypeItems(fieldUse).toArray(new TypeItem[0]);
    TypeItem.sort(typeItems);

    JBlock currentBlock = block;
    for (int index = typeItems.length - 1; index >= 0; index--)
    {
      final TypeItem typeItem = typeItems[index];
      final JExpression valueInstanceOfTypeItem = instanceofTypeItem(value, typeItem);
      final JConditional _if = currentBlock._if(valueInstanceOfTypeItem);
      final JType itemType = typeItem.getType().isPrimitive() ?
        ((JPrimitiveType) typeItem.getType()).getWrapperClass() : typeItem.getType();
      final JVar realValue = _if._then().decl(itemType, "realValue", JExpr.cast(itemType, value));
      _if._then().add(generateValueCheck(fieldUse, typeItem, locator, master, handler, realValue));
      currentBlock = _if._else();
    }
    currentBlock.directStatement("// Report unexpected value");
    return block;
  }

  protected JStatement generateCollectionFieldCheck(final FieldUse fieldUse, JVar handler, JVar locator, JVar master, final JExpression values)
  {
    final JBlock block = JBlock.dummyInstance.block();
    block.directStatement("// todo: collection field check");
    final JForLoop _for = block._for();
    final JVar index = _for.init(codeModel.INT, "index", JExpr.lit(0));
    _for.test(JOp.lt(index, values.invoke("size")));
    final JVar item = _for.body().decl(codeModel.ref(Object.class), "item", JExpr.invoke(values, "get").arg(index));
    final JExpression entryLocator = JExpr._new(codeModel.ref(EntryLocator.class)).arg(locator).arg(master).arg(JExpr.lit(fieldUse.name)).arg(index);
    _for.body().add(generateSingleFieldCheck(fieldUse, handler, entryLocator, master, item));
    return block;
  }

  protected JStatement generateValueCheck(final FieldUse fieldUse, final TypeItem typeItem, final JExpression locator, final JVar master, final JVar handler, final JVar value)
  {
    final JBlock block = JBlock.dummyInstance.block();
    final JType valueType = value.type();
    if (typeItem instanceof PrimitiveItem)
    {
      block.directStatement("// Check primitive value");
      block.add(generatePrimitiveValueCheck(fieldUse, (PrimitiveItem) typeItem, locator, master, handler, value));
    }
    else if (typeItem instanceof ClassItem)
    {
      block.directStatement("// Check complex value");
      final JDefinedClass valueClass = (JDefinedClass) valueType;
      final JDefinedClass verifierClass = getOrGenerateEditorClass(valueClass);
      final JVar verifier = block.decl(verifierClass, "verifier", JExpr._new(verifierClass));
      block.invoke(verifier, "check").arg(locator).arg(handler).arg(value);
    }
    return block;
  }

  protected JStatement generatePrimitiveValueCheck(final FieldUse fieldUse, final PrimitiveItem item, JExpression locator, final JVar master, final JVar handler, final JVar value)
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
        final JVar problem = block.decl(codeModel.ref(Problem.class), "problem", JExpr._null());
        block.add(vc.verify(guard, codeModel, value, problem));
        final JConditional ifProblemIsNotNull = block._if(JOp.ne(JExpr._null(), problem));
        ifProblemIsNotNull._then().directStatement("// Handle event");
        ifProblemIsNotNull._then().invoke(handler, "handleEvent").arg(JExpr._new(codeModel.ref(DatatypeEvent.class)).arg(locator).arg(problem));
      }
    }
    return block;
  }


  protected JMethod generateCheck()
  {
    final JMethod check = theClass.method(JMod.PUBLIC, codeModel.VOID, "check");
    final JVar parentLocator = check.param(codeModel.ref(VerificationEventLocator.class), "parentLocator");
    final JVar handler = check.param(codeModel.ref(ValidationEventHandler.class), "handler");
    final JVar master = check.param(classContext.ref, "master");
    final JStatement statement = (JStatement) expression.visit(new CheckVisitor(handler, parentLocator, master));
    check.body().add(statement);
    return check;
  }

  public class CheckVisitor extends BGMVisitor
  {
    protected JExpression master;
    protected JExpression handler;
    protected JExpression locator;

    public CheckVisitor(final JExpression handler, JExpression parentLocator, final JExpression master)
    {
      this.master = master;
      this.handler = handler;
      this.locator = parentLocator;
    }

    public Object onOccurrenceExp(final OccurrenceExp exp)
    {
      final JBlock block = (JBlock) exp.itemExp.visit(this);
      return block;
    }

    public Object onOther(final OtherExp exp)
    {
      if (exp instanceof OccurrenceExp)
      {
        return onOccurrenceExp((OccurrenceExp) exp);
      }
      else
      {
        return super.onOther(exp);
      }
    }

    public Object onClass(final ClassItem item)
    {
      return null;
    }

    public Object onExternal(final ExternalItem item)
    {
      return null;
    }

    public Object onField(final FieldItem fieldItem)
    {
      final JBlock block = JBlock.dummyInstance.block();

      final JExpression locator = JExpr._new(codeModel.ref(VerificationEventLocator.class)).arg(this.locator).arg(master).arg(JExpr.lit(fieldItem.name));

      final FieldUse fieldUse = classContext.target.getField(fieldItem.name);

      final JMethod getter = Util.getFieldInterfaceGetter(classContext, fieldItem);

      JBlock checkBlock = block;
      // If field is not a constant field
      if (null != getter)
      {
        final JExpression field = JExpr.invoke(master, getter);

        if (fieldUse.type.isPrimitive())
        {
          block.directStatement("// No check for primitive values");
        }
        else
        {
          /// if (null == master.get<Field>()
          final JConditional ifNullConditional = block._if(JOp.eq(JExpr._null(), field));
          final JBlock ifNull = ifNullConditional._then();
          final JBlock ifNotNull = ifNullConditional._else();

          // A, A?
          if (fieldUse.multiplicity.isAtMostOnce())
          {
            if (fieldUse.multiplicity.isOptional())
            {
              ifNull.directStatement("// Optional field - nothing to report");
            }
            else
            {
              ifNull.directStatement("// Report missing object");
//              ifNull.invoke(report).arg(master).arg(JExpr.lit(fieldItem.name));
            }
          }
          // A*, A+
          else
          {
            if (fieldItem.multiplicity.isOptional())
            {
              ifNull.directStatement("// Optional - nothing to report");
            }
            else
            {
              ifNull.directStatement("// Report missing object");
//              ifNull.invoke(report).arg(master).arg(JExpr.lit(fieldItem.name));
            }
            ifNotNull.directStatement("// Check count");
            final JExpression count = JExpr.invoke(field, "size");

            /// if (getField().size() < minOccurs)
            final JBlock minOccursViolated = ifNotNull.
              _if(JOp.lt(count, JExpr.lit(fieldUse.multiplicity.min)))._then();

            minOccursViolated.directStatement("// Report minimum of occurences violated");

            // If maxOccurs is not "unbounded"
            if (fieldUse.multiplicity.max != null)
            {
              /// if (getField().size() > maxOccurs)
              final JBlock maxOccursViolated = ifNotNull.
                _if(JOp.gt(count, JExpr.lit(fieldUse.multiplicity.max.intValue())))._then();

              maxOccursViolated.directStatement("// Report maximum of occurences violated");
            }
          }
          ifNotNull.directStatement("// Check value");
          checkBlock = ifNotNull;
        }
        checkBlock.invoke("check" + fieldUse.name).arg(locator).arg(handler).arg(master).
          arg((fieldUse.multiplicity.isAtMostOnce() && fieldUse.type.isPrimitive()) ? ((JPrimitiveType) fieldUse.type).wrap(field) : field);
      }
      return block;
    }

    public Object onIgnore(final IgnoreItem item)
    {
      final JBlock block = JBlock.dummyInstance.block();
      return block;
    }

    public Object onInterface(final InterfaceItem item)
    {
      return null;
    }

    public Object onPrimitive(final PrimitiveItem item)
    {
      return null;
    }

    public Object onSuper(final SuperClassItem item)
    {
      final JBlock block = JBlock.dummyInstance.block();
      block.invoke(JExpr.ref("super"), "check").arg(locator).arg(handler).arg(master);
      return block;
    }

    public Object onAnyString()
    {
      return null;
    }

    public Object onEpsilon()
    {
      final JBlock block = JBlock.dummyInstance.block();
      block.directStatement("// epsilon - nothing to report");
      return block;
    }

    public Object onNullSet()
    {
      return null;
    }

    public Object onAttribute(final AttributeExp exp)
    {
      return null;
    }

    public Object onConcur(final ConcurExp p)
    {
      return null;
    }

    public Object onData(final DataExp exp)
    {
      return null;
    }

    public Object onElement(final ElementExp exp)
    {
      final JBlock block = JBlock.dummyInstance.block();
      final JStatement statement = (JStatement) exp.contentModel.visit(this);
      block.add(statement);
      return block;
    }

    public Object onInterleave(final InterleaveExp p)
    {
      return null;
    }

    public Object onList(final ListExp exp)
    {
      return null;
    }

    public Object onMixed(final MixedExp exp)
    {
      return null;
    }

    public Object onOneOrMore(final OneOrMoreExp exp)
    {
      final JBlock block = (JBlock) exp.exp.visit(this);
      return block;
    }

    public Object onRef(final ReferenceExp exp)
    {
      return null;
    }

    public Object onSequence(final SequenceExp exp)
    {
      final JBlock block = JBlock.dummyInstance.block();
      final JBlock left = (JBlock) exp.exp1.visit(this);
      final JBlock right = (JBlock) exp.exp2.visit(this);
      block.add(left);
      block.add(right);
      return block;
    }

    public Object onChoice(final ChoiceExp exp)
    {
      final JBlock block = JBlock.dummyInstance.block();
      final JExpression leftExists = (JExpression) exp.exp1.visit(new ExistsVisitor(master));
      final JExpression rightExists = (JExpression) exp.exp2.visit(new ExistsVisitor(master));

      if (exp.exp2 == Expression.epsilon)
      {
        final JConditional l = block._if(leftExists);
        l._then().directStatement("// If left exists");
        l._then().add((JBlock) exp.exp1.visit(this));
        return block;
      }
      else if (exp.exp1 == Expression.epsilon)
      {
        final JConditional r = block._if(rightExists);
        r._then().directStatement("// If right exists");
        r._then().add((JBlock) exp.exp2.visit(this));
        return block;
      }
      else
      {
        block.directStatement("// Choice");
        final JConditional lnr = block._if(JOp.cand(leftExists, JOp.not(rightExists)));
        lnr._then().directStatement("// If left exists");
        lnr._then().add((JBlock) exp.exp1.visit(this));
        final JConditional nlr = lnr._else()._if(JOp.cand(JOp.not(leftExists), rightExists));
        nlr._then().directStatement("// If right exists");
        final JConditional nlnr = nlr._else()._if(JOp.cand(JOp.not(leftExists), JOp.not(rightExists)));
        nlr._then().add((JBlock) exp.exp2.visit(this));
        nlnr._then().directStatement("// If none exist");
        nlnr._else().directStatement("// If both present");
      }
      return block;
    }


    public Object onValue(final ValueExp exp)
    {
      return null;
    }

  }

  public class ExistsVisitor extends BGMVisitor
  {
    protected JExpression master;

    public ExistsVisitor(final JExpression master)
    {
      this.master = master;
    }

    public Object onField(final FieldItem item)
    {
      // todo: Use isSet methods to detect if field was set.
      final JMethod getter = Util.getFieldInterfaceGetter(classContext, item);
      final JType type = item.getType(codeModel);
      if (type.isPrimitive())
      {
        // We can't check if primitive value was set or not, so we'll assume it always is set.
        return JExpr.TRUE;
      }
      else
      {
        final JExpression notNull = JOp.ne(JExpr._null(), JExpr.invoke(master, getter));
        if (!item.multiplicity.isAtMostOnce())
        {
          return JOp.cand(notNull, JOp.gt(JExpr.invoke(JExpr.invoke(master, getter), "size"), JExpr.lit(1)));
        }
        else
        {
          return notNull;
        }
      }
    }

    public Object onClass(final ClassItem item)
    {
      return null;
    }

    public Object onExternal(final ExternalItem item)
    {
      return null;
    }

    public Object onIgnore(final IgnoreItem item)
    {
      return null;
    }

    public Object onInterface(final InterfaceItem item)
    {
      return null;
    }

    public Object onPrimitive(final PrimitiveItem item)
    {
      return null;
    }

    public Object onSuper(final SuperClassItem item)
    {
      return null;
    }

    public Object onAnyString()
    {
      return null;
    }

    public Object onEpsilon()
    {
      return JExpr.TRUE;
    }

    public Object onNullSet()
    {
      return null;
    }

    public Object onAttribute(final AttributeExp exp)
    {
      return null;
    }


    public Object onConcur(final ConcurExp p)
    {
      return null;
    }

    public Object onData(final DataExp exp)
    {
      return null;
    }

    public Object onElement(final ElementExp exp)
    {
      return JExpr.TRUE;
    }

    public Object onInterleave(final InterleaveExp p)
    {
      return null;
    }

    public Object onList(final ListExp exp)
    {
      return null;
    }

    public Object onMixed(final MixedExp exp)
    {
      return null;
    }

    public Object onRef(final ReferenceExp exp)
    {
      return null;
    }

    public Object onSequence(final SequenceExp exp)
    {
      final JExpression left = (JExpression) exp.exp1.visit(this);
      final JExpression right = (JExpression) exp.exp2.visit(this);
      if (left == JExpr.TRUE)
      {
        if (right == JExpr.TRUE)
        {
          return JExpr.TRUE;
        }
        else
        {
          return right;
        }
      }
      else if (right == JExpr.TRUE)
      {
        return left;
      }
      else
      {
        return JOp.cand(left, right);
      }
    }

    public Object onChoice(final ChoiceExp exp)
    {
      if (exp.exp1 == Expression.epsilon || exp.exp2 == Expression.epsilon)
      {
        return JExpr.TRUE;
      }
      else
      {
        final JExpression left = (JExpression) exp.exp1.visit(this);
        final JExpression right = (JExpression) exp.exp2.visit(this);

        if (left == JExpr.TRUE)
        {
          if (right == JExpr.FALSE)
          {
            return JExpr.TRUE;
          }
          else if (right == JExpr.TRUE)
          {
            return JExpr.FALSE;
          }
          else
          {
            return JOp.not(right);
          }
        }
        if (left == JExpr.FALSE)
        {
          if (right == JExpr.FALSE)
          {
            return JExpr.FALSE;
          }
          else if (right == JExpr.TRUE)
          {
            return JExpr.TRUE;
          }
          else
          {
            return right;
          }
        }
        else if (right == JExpr.FALSE)
        {
          return left;
        }
        else if (right == JExpr.TRUE)
        {
          return JOp.not(left);
        }
        else
        {
          return JOp.cor(JOp.cand(left, JOp.not(right)), JOp.cand(JOp.not(left), right));
        }
      }
    }

    public Object onOneOrMore(final OneOrMoreExp exp)
    {
      return exp.exp.visit(this);
    }

    public Object onValue(final ValueExp exp)
    {
      return null;
    }
  }

  public FieldItem copyFieldItem(final FieldItem item)
  {
    final FieldItem newItem = new FieldItem(item.name, item.exp, item.userSpecifiedType, item.locator);

    newItem.collisionExpected = item.collisionExpected;
//    newItem.
    return newItem;
  }

  /**
   * Creates editor class for the given content class.
   *
   * @param theClass content class to create editor class for.
   * @return Created class.
   */
  public static JDefinedClass getOrGenerateEditorClass(final JDefinedClass theClass)
  {
    final JClassContainer container = theClass.outer() instanceof JDefinedClass ?
      (JClassContainer) getOrGenerateEditorClass((JDefinedClass) theClass.outer()) :
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


}
