package de.fzi.dbs.verification.addon.datatype.atomic;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.UnicodeUtil;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;
import de.fzi.dbs.verification.addon.datatype.DiscreteVC;
import de.fzi.dbs.verification.event.datatype.AnyURIProblem;
import de.fzi.dbs.verification.util.ValidationUtils;

/**
 * VC for anyURI type.
 *
 * @author Aleksei Valikov
 */
public class AnyURITypeVC extends AbstractVC implements DiscreteVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final JConditional ifIsAnyURI = block._if(codeModel.ref(de.fzi.dbs.verification.util.ValidationUtils.class).staticInvoke("isAnyURI").arg(value));
    ifIsAnyURI._then().directStatement("// This value is a valid URI");
    ifIsAnyURI._else().
      assign(problem, JExpr._new(codeModel.ref(AnyURIProblem.class)).arg(value));
    return block;
  }

  public JExpression countLength(final JCodeModel codeModel, final JExpression value)
  {
    return codeModel.ref(UnicodeUtil.class).staticInvoke("countLength").arg(value);
  }

  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr.lit(object.toString());
  }
}
