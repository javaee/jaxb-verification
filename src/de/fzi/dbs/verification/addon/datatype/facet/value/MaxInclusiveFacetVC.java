package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import de.fzi.dbs.verification.event.datatype.LessProblem;
import de.fzi.dbs.verification.event.datatype.GreaterProblem;

/**
 * Max-inclusive facet.
 *
 * @author Aleksei Valikov
 */
public class MaxInclusiveFacetVC extends RangeFacetVC
{
  public JExpression rangeValid(final JCodeModel codeModel, final JExpression range)
  {
    return JOp.lte(range, JExpr.lit(0));
  }

  public JClass problemClass(final JCodeModel codeModel)
  {
    return codeModel.ref(GreaterProblem.class);
  }

}
