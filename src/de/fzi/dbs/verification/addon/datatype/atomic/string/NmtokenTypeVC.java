package de.fzi.dbs.verification.addon.datatype.atomic.string;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.XmlNames;
import de.fzi.dbs.verification.event.datatype.NmtokenProblem;

/**
 * VC for nmtoken type.
 *
 * @author Aleksei Valikov
 */
public class NmtokenTypeVC extends TokenTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JConditional ifValueIsNmtoken = block._if(codeModel.ref(XmlNames.class).staticInvoke("isNmtoken").arg(value));
    ifValueIsNmtoken._then().directStatement("// Value is a valid Nmtoken");
    ifValueIsNmtoken._else().
      assign(problem, JExpr._new(codeModel.ref(NmtokenProblem.class)).arg(value));
    return block;
  }
}
