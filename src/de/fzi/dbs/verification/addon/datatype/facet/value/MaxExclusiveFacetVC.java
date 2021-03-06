package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import de.fzi.dbs.verification.event.datatype.LessOrEqualProblem;
import de.fzi.dbs.verification.event.datatype.GreaterOrEqualProblem;

/**
 * Max-exclusive facet.
 *
 * @author Aleksei Valikov
 */
public class MaxExclusiveFacetVC extends RangeFacetVC
{
  public JExpression rangeValid(final JCodeModel codeModel, final JExpression range)
  {
    return JOp.lt(range, JExpr.lit(0));
  }

  public JClass problemClass(final JCodeModel codeModel)
  {
    return codeModel.ref(GreaterOrEqualProblem.class);
  }

}
