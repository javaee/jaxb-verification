package de.fzi.dbs.verification.addon.datatype.atomic.number;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.msv.datatype.DatabindableDatatype;

/**
 * VC for short type.
 *
 * @author Aleksei Valikov
 */
public class ShortTypeVC extends IntegerDerivedTypeVC
{
  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr._new(codeModel.ref(Short.class)).arg(JExpr.lit(((Short) object).shortValue()));
  }

}
