package de.fzi.dbs.verification.datatype.facet.lexical;

import com.sun.codemodel.JStatement;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;

/**
 * VC for pattern facets.
 *
 * @author Aleksei Valikov
 */
public class PatternFacetVC extends DataTypeWithLexicalConstraintFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    block.directStatement("// todo: check via regular expression");
    return block;
  }
}
