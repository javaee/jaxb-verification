package de.fzi.dbs.verification.addon.datatype.atomic;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.UnicodeUtil;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;
import de.fzi.dbs.verification.addon.datatype.DiscreteVC;

/**
 * VC for QName type.
 *
 * @author Aleksei Valikov
 */
public class QnameTypeVC extends AbstractVC implements DiscreteVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    block.directStatement("// QName is assumed to be always valid");
    return block;
  }

  public JExpression countLength(final JCodeModel codeModel, final JExpression value)
  {
    final JClass unicodeUtil = codeModel.ref(UnicodeUtil.class);
    return JOp.plus(unicodeUtil.staticInvoke("countLength").arg(value.invoke("getNamespaceURI")),
      unicodeUtil.staticInvoke("countLength").arg(value.invoke("getLocalPart")));
  }

  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr.lit(object.toString());
  }
}
