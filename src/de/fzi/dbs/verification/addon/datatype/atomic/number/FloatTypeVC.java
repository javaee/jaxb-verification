package de.fzi.dbs.verification.addon.datatype.atomic.number;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.msv.datatype.DatabindableDatatype;

/**
 * VC for float type.
 *
 * @author Aleksei Valikov
 */
public class FloatTypeVC extends FloatingNumberTypeVC
{
  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr._new(codeModel.ref(Float.class)).arg(JExpr.lit(((Float) object).floatValue()));
  }

}
