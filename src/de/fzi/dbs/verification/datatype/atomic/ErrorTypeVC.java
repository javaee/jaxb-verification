package de.fzi.dbs.verification.datatype.atomic;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.datatype.AbstractVC;

/**
 * VC for error type.
 *
 * @author Aleksei Valikov
 */
public class ErrorTypeVC extends AbstractVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    block.directStatement("// Fallback type: no validation rules");
    return block;
  }
}
