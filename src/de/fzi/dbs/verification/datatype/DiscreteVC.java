package de.fzi.dbs.verification.datatype;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;

/**
 * Discrete VC is capable of counting value length.
 *
 * @author Aleksei Valikov
 */
public interface DiscreteVC
{
  /**
   * Constructs an expression that returns the length of the value.
   *
   * @param codeModel code model.
   * @param value     value.
   * @return Expression that returns the length of the value.
   */
  public JExpression countLength(JCodeModel codeModel, JExpression value);
}
