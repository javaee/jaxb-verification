package de.fzi.dbs.verification.addon.datatype.atomic.number;

import java.math.BigInteger;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.IntegerValueType;

/**
 * VC for integer type.
 *
 * @author Aleksei Valikov
 */
public class IntegerTypeVC extends IntegerDerivedTypeVC
{
  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    final BigInteger bigInteger = ((IntegerValueType) object).toBigInteger();
    return JExpr._new(codeModel.ref(BigInteger.class)).arg(JExpr.lit(bigInteger.toString()));
  }
}
