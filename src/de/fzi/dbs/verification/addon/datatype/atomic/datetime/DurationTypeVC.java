package de.fzi.dbs.verification.addon.datatype.atomic.datetime;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.datetime.ITimeDurationValueType;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;
import de.fzi.dbs.verification.addon.datatype.ComparatorVC;

/**
 * VC for duration type.
 *
 * @author Aleksei Valikov
 */
public class DurationTypeVC extends AbstractVC implements ComparatorVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    return block;
  }

  public JExpression compare(final JCodeModel codeModel, final JExpression o1, final JExpression o2)
  {
    return JExpr.invoke(JExpr.cast(codeModel.ref(ITimeDurationValueType.class), o1), "lengthValid").arg(JExpr.cast(codeModel.ref(ITimeDurationValueType.class), o2));
  }

  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    // todo: ???
    return JExpr.lit(object.toString());
  }
}
