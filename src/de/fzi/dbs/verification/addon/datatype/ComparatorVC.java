package de.fzi.dbs.verification.addon.datatype;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;

/**
 * Comparing VC is capable of creating comparison expression for two objects.
 *
 * @author Aleksei Valikov
 */
public interface ComparatorVC
{
  /**
   * Constructs a comparison expression for two objects.
   *
   * @param codeModel code model.
   * @param o1        first comparison argument.
   * @param o2        second comparison argument.
   * @return Comparison statement.
   */
  public JExpression compare(JCodeModel codeModel, JExpression o1, JExpression o2);
}
