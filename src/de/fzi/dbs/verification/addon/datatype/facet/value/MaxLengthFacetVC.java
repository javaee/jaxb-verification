package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import com.sun.msv.datatype.xsd.MaxLengthFacet;
import de.fzi.dbs.verification.event.datatype.TooLongProblem;

/**
 * Max length facet.
 *
 * @author Aleksei Valikov
 */
public class MaxLengthFacetVC extends BaseLengthFacetVC
{
  public int expectedLength(final DataTypeWithFacet datatype)
  {
    return ((MaxLengthFacet) datatype).maxLength;
  }

  public JClass problemClass(final JCodeModel codeModel)
  {
    return codeModel.ref(de.fzi.dbs.verification.event.datatype.TooLongProblem.class);
  }

  public JExpression lengthValid(final JCodeModel codeModel, final JExpression length, final JExpression expectedLength)
  {
    return JOp.lte(length, expectedLength);
  }
}
