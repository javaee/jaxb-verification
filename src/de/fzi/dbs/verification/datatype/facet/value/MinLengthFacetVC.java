package de.fzi.dbs.verification.datatype.facet.value;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import com.sun.msv.datatype.xsd.MinLengthFacet;
import de.fzi.dbs.verification.datatype.problem.TooShortProblem;

/**
 * Min length facet.
 *
 * @author Aleksei Valikov
 */
public class MinLengthFacetVC extends BaseLengthFacetVC
{
  public int expectedLength(final DataTypeWithFacet datatype)
  {
    return ((MinLengthFacet) datatype).minLength;
  }

  public JClass problemClass(final JCodeModel codeModel)
  {
    return codeModel.ref(TooShortProblem.class);
  }

  public JExpression lengthValid(final JCodeModel codeModel, final JExpression length, final JExpression expectedLength)
  {
    return JOp.gte(length, expectedLength);
  }
}
