package de.fzi.dbs.verification;

import com.sun.codemodel.JDefinedClass;

/**
 * Verification add-on utilities.
 *
 * @author Aleksei Valikov
 */
public class Util
{
  /**
   * Hidden constructor.
   */
  private Util()
  {
  }

  /**
   * Returns a new unique name of the field in the given class, based on the given prefix. If required, the prefix will
   * be appended with an integer to make it unique
   *
   * @param theClass class to create field in.
   * @param prefix   field name prefix.
   * @return Unique name of the new field.
   */
  public static String generateFieldName(final JDefinedClass theClass, final String prefix)
  {
    final String name;
    if (null == de.fzi.dbs.jaxb.addon.Util.getField(theClass, prefix))
    {
      name = prefix;
    }
    else
    {
      int index = 0;
      while (null != de.fzi.dbs.jaxb.addon.Util.getField(theClass, prefix + index))
      {
        index++;
      }
      name = prefix + index;
    }
    return name;
  }
}
