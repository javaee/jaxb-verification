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
import de.fzi.dbs.verification.event.datatype.GreaterProblem;
import de.fzi.dbs.verification.event.datatype.NegativeProblem;

/**
 * VC for unsignedShort type.
 *
 * @author Aleksei Valikov
 */
public class UnsignedShortTypeVC extends de.fzi.dbs.verification.addon.datatype.atomic.number.IntTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JExpression intValue = value.invoke("intValue");
    final JConditional ifNegative = block._if(JOp.lt(intValue, JExpr.lit(0)));
    ifNegative._then().assign(problem, JExpr._new(codeModel.ref(de.fzi.dbs.verification.event.datatype.NegativeProblem.class)).arg(value));
    final JConditional ifTooBig = ifNegative._else()._if(JOp.gt(intValue, JExpr.lit(65535)));
    ifTooBig._then().assign(problem,
      JExpr._new(codeModel.ref(GreaterProblem.class)).arg(value).arg(JExpr._new(codeModel.ref(Integer.class)).arg(JExpr.lit(65535))));
    return block;
  }

}
