package de.fzi.dbs.verification.addon.datatype.atomic.datetime;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.Comparator;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;
import de.fzi.dbs.verification.addon.datatype.ComparatorVC;

import java.util.Calendar;

/**
 * Base VC for date/time types.
 *
 * @author Aleksei Valikov
 */
public abstract class DateTimeBaseTypeVC extends AbstractVC implements ComparatorVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    return block;
  }

  public JExpression compare(final JCodeModel codeModel, final JExpression o1, final JExpression o2)
  {
    final JExpression left = JExpr.cast(codeModel.ref(Calendar.class), o1);
    final JExpression right = JExpr.cast(codeModel.ref(Calendar.class), o2);
    return JOp.cond(left.invoke("before").arg(right),
      JExpr.lit(Comparator.LESS),
      JOp.cond(left.invoke("after").arg(right),
        JExpr.lit(Comparator.GREATER), JExpr.lit(Comparator.EQUAL)));
  }
}
