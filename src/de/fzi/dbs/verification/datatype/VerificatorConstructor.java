package de.fzi.dbs.verification.datatype;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;

/**
 * Constructor of datatype verification statements.
 *
 * @author Aleksei Valikov
 */
public interface VerificatorConstructor
{
  /**
   * Constructs a statement that verifies given value.
   *
   * @param datatype  datatype.
   * @param codeModel code model.
   * @param value     value expression to be verified.
   * @param problem   variable that a problem should be assigned to.
   * @return Value verification statement.
   */
  public JStatement verify(DatabindableDatatype datatype, JCodeModel codeModel, JExpression value, JAssignmentTarget problem);

}
