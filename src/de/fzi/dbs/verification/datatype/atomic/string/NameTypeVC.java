package de.fzi.dbs.verification.datatype.atomic.string;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.XmlNames;
import de.fzi.dbs.verification.datatype.problem.NameProblem;

/**
 * VC for name type.
 *
 * @author Aleksei Valikov
 */
public class NameTypeVC extends TokenTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JConditional ifValueIsName = block._if(codeModel.ref(XmlNames.class).staticInvoke("isName").arg(value));
    ifValueIsName._then().directStatement("// Value is a valid Name");
    ifValueIsName._else().
      assign(problem, JExpr._new(codeModel.ref(NameProblem.class)).arg(value));
    return block;
  }
}
