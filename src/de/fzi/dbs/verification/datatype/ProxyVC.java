package de.fzi.dbs.verification.datatype;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JAssignmentTarget;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.Proxy;

/**
 * Proxy VC.
 */
public class ProxyVC extends AbstractVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final Proxy proxy = (Proxy) datatype;
    final VerificatorConstructor vc
      = VerificatorConstructorFactory.getVerificatorConstructor(proxy.getBaseType());
    return vc.verify(proxy.getBaseType(), codeModel, value, problem);
  }
}
