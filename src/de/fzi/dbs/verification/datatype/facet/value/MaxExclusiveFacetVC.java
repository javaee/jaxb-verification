package de.fzi.dbs.verification.datatype.facet.value;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import de.fzi.dbs.verification.datatype.problem.LessOrEqualProblem;

/**
 * Max-exclusive facet.
 *
 * @author Aleksei Valikov
 */
public class MaxExclusiveFacetVC extends RangeFacetVC
{
  public JExpression rangeValid(final JCodeModel codeModel, final JExpression range)
  {
    return JOp.gt(range, JExpr.lit(0));
  }

  public JClass problemClass(final JCodeModel codeModel)
  {
    return codeModel.ref(LessOrEqualProblem.class);
  }

}
