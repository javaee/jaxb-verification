package de.fzi.dbs.verification.datatype.atomic.binary;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.datatype.AbstractVC;
import de.fzi.dbs.verification.datatype.DiscreteVC;

/**
 * Base VC for binary types.
 *
 * @author Aleksei Valikov
 */
public abstract class BinaryBaseTypeVC extends AbstractVC implements DiscreteVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    return block;
  }

  public JExpression countLength(final JCodeModel codeModel, final JExpression value)
  {
    return value.ref("length");
  }
}
