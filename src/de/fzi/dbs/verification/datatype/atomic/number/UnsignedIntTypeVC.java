package de.fzi.dbs.verification.datatype.atomic.number;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.datatype.problem.GreaterProblem;
import de.fzi.dbs.verification.datatype.problem.NegativeProblem;

/**
 * VC for unsignedInt type.
 *
 * @author Aleksei Valikov
 */
public class UnsignedIntTypeVC extends de.fzi.dbs.verification.datatype.atomic.number.LongTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JExpression longValue = value.invoke("longValue");
    final JConditional ifNegative = block._if(JOp.lt(longValue, JExpr.lit(0)));
    ifNegative._then().assign(problem, JExpr._new(codeModel.ref(NegativeProblem.class)).arg(value));
    final JConditional ifTooBig = ifNegative._else()._if(JOp.gt(longValue, JExpr.lit(4294967295L)));
    ifTooBig._then().assign(problem,
      JExpr._new(codeModel.ref(GreaterProblem.class)).arg(value).arg(JExpr._new(codeModel.ref(Long.class)).arg(JExpr.lit(4294967295L))));
    return block;
  }
}