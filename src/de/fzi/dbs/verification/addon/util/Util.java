package de.fzi.dbs.verification.addon.util;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JResourceFile;
import com.sun.codemodel.JType;
import com.sun.codemodel.fmt.JPropertyFile;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.FieldItem;

import java.util.Iterator;

/**
 * Verification add-on utilities.
 *
 * @author Aleksei Valikov
 */
public class Util
{
  private Util()
  {
  }

  public static JPropertyFile getOrCreatePropertyFile(final JPackage thePackage, final String name)
  {
    JPropertyFile propertyFile = null;
    for (Iterator iterator = thePackage.propertyFiles(); iterator.hasNext() && (null == propertyFile);)
    {
      final JResourceFile resourceFile = (JResourceFile) iterator.next();
      if (resourceFile instanceof JPropertyFile && name.equals(resourceFile.name()))
      {
        propertyFile = (JPropertyFile) resourceFile;
      }
    }

    if (null == propertyFile)
    {
      propertyFile = new JPropertyFile(name);
      thePackage.addResourceFile(propertyFile);
    }
    return propertyFile;
  }

  /**
   * Returns field getter from the class.
   *
   * @param theClass  class.
   * @param fieldItem field.
   * @return Field's getter method.
   */
  public static JMethod getFieldGetter(final JDefinedClass theClass, final FieldItem fieldItem)
  {
    final String getterName = (fieldItem.getType(theClass.owner()) == theClass.owner().BOOLEAN ? "is" : "get") + fieldItem.name;
    return theClass.getMethod(getterName, new JType[0]);
  }

  /**
   * Returns field setter from the class.
   *
   * @param theClass  class.
   * @param fieldItem field.
   * @return Field's setter method.
   */
  public static JMethod getFieldSetter(final JDefinedClass theClass, final FieldItem fieldItem)
  {
    final String setterName = "set" + fieldItem.name;
    return theClass.getMethod(setterName, new JType[]{fieldItem.getType(theClass.owner())});
  }

  /**
   * Retrieves field's getter method from interface.
   *
   * @param classContext class context.
   * @param fieldItem    field use.
   * @return Field's getter method.
   */
  public static JMethod getFieldInterfaceGetter(final ClassContext classContext, final FieldItem fieldItem)
  {
    return getFieldGetter(classContext.ref, fieldItem);
  }

  /**
   * Retrieves field's getter method from interface.
   *
   * @param classContext class context.
   * @param fieldItem    field use.
   * @return Field's getter method.
   */
  public static JMethod getFieldClassGetter(final ClassContext classContext, final FieldItem fieldItem)
  {
    return getFieldGetter(classContext.implClass, fieldItem);
  }

  /**
   * Retrieves field's setter method from interface.
   * 
   * @param classContext class context.
   * @param fieldItem    field use.
   * @return Field's setter method.
   */
  public static JMethod getFieldInterfaceSetter(final ClassContext classContext, final FieldItem fieldItem)
  {
    return getFieldSetter(classContext.ref, fieldItem);
  }

  /**
   * Retrieves field's setter method from interface.
   *
   * @param classContext class context.
   * @param fieldItem    field use.
   * @return Field's setter method.
   */
  public static JMethod getFieldClassSetter(final ClassContext classContext, final FieldItem fieldItem)
  {
    return getFieldSetter(classContext.implClass, fieldItem);
  }

}
