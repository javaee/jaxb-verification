package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPrimitiveType;
import com.sun.codemodel.fmt.JPropertyFile;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.FieldUse;
import de.fzi.dbs.verification.addon.util.Util;

/**
 * Field-based generator. Generates <code>getName()</code> and <code>getPropertyClass</code> methods.
 *
 * @author Aleksei Valikov
 */
public abstract class FieldBasedGenerator extends ClassBasedGenerator
{
  /**
   * Field use.
   */
  protected FieldUse fieldUse;

  /**
   * Field type.
   */
  protected JClass type;

  /**
   * <code>getName()</code> method.
   */
  protected JMethod getName;

  /**
   * <code>getPropertyClass()</code> method.
   */
  protected JMethod getPropertyClass;

  /**
   * Constructs a new field-based generator.
   *
   * @param classContext class context.
   * @param fieldUse     field use.
   */
  protected FieldBasedGenerator(final ClassContext classContext, final FieldUse fieldUse)
  {
    super(classContext);
    this.fieldUse = fieldUse;
    this.type = fieldUse.type.isPrimitive() ? ((JPrimitiveType) fieldUse.type).getWrapperClass() :
      (JClass) fieldUse.type;
  }

  /**
   * Generates <code>getName()</code> method.
   *
   * @return Generated method.
   */
  protected JMethod generateGetName()
  {
    final JMethod getName = theClass.method(JMod.PUBLIC, String.class, "getName");
    final String key = fieldUse.owner.getType().fullName() + "." + fieldUse.name;
    addMessage(key, key);
    getName.body()._return(JExpr.lit(key));
    return getName;
  }

  /**
   * Generates <code>getPropertyClass()</code> method.
   *
   * @return Generated method.
   */
  protected JMethod generateGetPropertyClass()
  {
    final JMethod getPropertyClass = theClass.method(JMod.PUBLIC, Class.class, "getPropertyClass");
    getPropertyClass.body()._return(type.staticRef("class"));
    return getPropertyClass;
  }

  protected void generateFields()
  {
  }

  protected void generateMethods()
  {
    getName = generateGetName();
    getPropertyClass = generateGetPropertyClass();
  }

  protected void addMessage(final String key, final String value)
  {
    final JPropertyFile propertyFile = Util.getOrCreatePropertyFile(theClass._package(), "Messages.properties");
    propertyFile.add(key, value);
  }
}
