package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import com.sun.msv.datatype.xsd.LengthFacet;
import de.fzi.dbs.verification.event.datatype.WrongLengthProblem;

/**
 * Length facet.
 *
 * @author Aleksei Valikov
 */
public class LengthFacetVC extends BaseLengthFacetVC
{
  public int expectedLength(final DataTypeWithFacet datatype)
  {
    return ((LengthFacet) datatype).length;
  }

  public JClass problemClass(final JCodeModel codeModel)
  {
    return codeModel.ref(WrongLengthProblem.class);
  }

  public JExpression lengthValid(final JCodeModel codeModel, final JExpression length, final JExpression expectedLength)
  {
    return JOp.eq(length, expectedLength);
  }
}
