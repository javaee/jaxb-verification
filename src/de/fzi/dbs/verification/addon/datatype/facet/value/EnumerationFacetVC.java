package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;

/**
 * Enumeration facet.
 *
 * @author Aleksei Valikov
 */
public class EnumerationFacetVC extends DataTypeWithValueConstraintFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    block.directStatement("// todo: check values of the enumeration");
    return block;
  }
}
