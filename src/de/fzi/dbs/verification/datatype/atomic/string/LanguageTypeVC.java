package de.fzi.dbs.verification.datatype.atomic.string;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.datatype.problem.LanguageProblem;
import de.fzi.dbs.verification.datatype.util.ValidationUtils;

/**
 * VC for language type.
 *
 * @author Aleksei Valikov
 */
public class LanguageTypeVC extends TokenTypeVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JConditional ifValueIsLanguage = block._if(codeModel.ref(ValidationUtils.class).staticInvoke("isLanguage").arg(value));
    ifValueIsLanguage._then().directStatement("// Value is a valid Language");
    ifValueIsLanguage._else().
      assign(problem, JExpr._new(codeModel.ref(LanguageProblem.class)).arg(value));
    return block;
  }
}
