package de.fzi.dbs.verification.addon.datatype.atomic.number;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.msv.datatype.DatabindableDatatype;

import java.math.BigInteger;

/**
 * VC for integer type.
 *
 * @author Aleksei Valikov
 */
public class IntegerTypeVC extends IntegerDerivedTypeVC
{
  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    final BigInteger bigInteger = (BigInteger) object;
    return JExpr._new(codeModel.ref(BigInteger.class)).arg(JExpr.lit(bigInteger.toString()));
  }
}
