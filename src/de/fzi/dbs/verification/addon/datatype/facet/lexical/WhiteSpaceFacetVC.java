package de.fzi.dbs.verification.addon.datatype.facet.lexical;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import de.fzi.dbs.verification.addon.datatype.facet.DataTypeWithFacetVC;

/**
 * VC for whitespace facet.
 *
 * @author Aleksei Valikov
 */
public class WhiteSpaceFacetVC extends DataTypeWithFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    block.directStatement("// todo: check the semantics");
    block.directStatement("// WhiteSpace facet is always valid");
    return block;
  }
}
