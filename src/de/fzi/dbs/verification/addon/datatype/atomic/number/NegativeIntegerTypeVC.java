package de.fzi.dbs.verification.addon.datatype.atomic.number;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.event.datatype.NonNegativeProblem;

/**
 * VC for negativeInteger type.
 *
 * @author Aleksei Valikov
 */
public class NegativeIntegerTypeVC extends IntegerTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JExpression signum = value.invoke("signum");
    final JConditional ifRangeViolated = block._if(JOp.ne(signum, JExpr.lit(-1)));
    ifRangeViolated._then().assign(problem,
      JExpr._new(codeModel.ref(de.fzi.dbs.verification.event.datatype.NonNegativeProblem.class)).arg(value));
    return block;
  }
}
