package de.fzi.dbs.verification.datatype.atomic.number;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.datatype.AbstractVC;
import de.fzi.dbs.verification.datatype.ComparatorVC;

/**
 * VC for integer-derived types.
 *
 * @author Aleksei Valikov
 */
public abstract class IntegerDerivedTypeVC extends AbstractVC implements ComparatorVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    return block;
  }

  public JExpression compare(final JCodeModel codeModel, final JExpression o1, final JExpression o2)
  {
    return JExpr.invoke(JExpr.cast(codeModel.ref(Comparable.class), o1), "compareTo").arg(o2);
  }
}