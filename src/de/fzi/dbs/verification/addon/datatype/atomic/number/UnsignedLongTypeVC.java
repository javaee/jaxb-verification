package de.fzi.dbs.verification.addon.datatype.atomic.number;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.event.datatype.GreaterProblem;
import de.fzi.dbs.verification.event.datatype.NegativeProblem;

import java.math.BigInteger;

/**
 * VC for unsignedLong type.
 *
 * @author Aleksei Valikov
 */
public class UnsignedLongTypeVC extends IntegerTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JExpression signum = value.invoke("signum");
    final JConditional ifNegative = block._if(JOp.lt(signum, JExpr.lit(0)));
    ifNegative._then().assign(problem, JExpr._new(codeModel.ref(NegativeProblem.class)).arg(value));
    final JConditional ifTooBig = ifNegative._else()._if(JOp.gt(JExpr.invoke(value, "compareTo").arg(JExpr._new(codeModel.ref(BigInteger.class)).arg(JExpr.lit("18446744073709551615"))), JExpr.lit(0)));
    ifTooBig._then().assign(problem,
      JExpr._new(codeModel.ref(de.fzi.dbs.verification.event.datatype.GreaterProblem.class)).arg(value).arg(JExpr._new(codeModel.ref(BigInteger.class)).arg(JExpr.lit("18446744073709551615"))));
    return block;
  }

}
