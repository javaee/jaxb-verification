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

/**
 * VC for unsignedInt type.
 *
 * @author Aleksei Valikov
 */
public class UnsignedIntTypeVC extends de.fzi.dbs.verification.addon.datatype.atomic.number.LongTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JExpression longValue = value.invoke("longValue");
    final JConditional ifNegative = block._if(JOp.lt(longValue, JExpr.lit(0)));
    ifNegative._then().assign(problem, JExpr._new(codeModel.ref(de.fzi.dbs.verification.event.datatype.NegativeProblem.class)).arg(value));
    final JConditional ifTooBig = ifNegative._else()._if(JOp.gt(longValue, JExpr.lit(4294967295L)));
    ifTooBig._then().assign(problem,
      JExpr._new(codeModel.ref(GreaterProblem.class)).arg(value).arg(JExpr._new(codeModel.ref(Long.class)).arg(JExpr.lit(4294967295L))));
    return block;
  }

  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr._new(codeModel.ref(Long.class)).arg(JExpr.lit(((Long) object).longValue()));
  }

}
