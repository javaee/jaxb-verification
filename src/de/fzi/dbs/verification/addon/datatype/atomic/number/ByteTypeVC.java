package de.fzi.dbs.verification.addon.datatype.atomic.number;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.msv.datatype.DatabindableDatatype;

/**
 * VC for byte type.
 *
 * @author Aleksei Valikov
 */
public class ByteTypeVC extends IntegerDerivedTypeVC
{
  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr._new(codeModel.ref(Byte.class)).arg(JExpr.lit(((Byte) object).byteValue()));
  }
}
