package de.fzi.dbs.verification.addon.datatype.atomic.datetime;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.Comparator;
import com.sun.msv.datatype.xsd.datetime.BigDateTimeValueType;
import com.sun.msv.datatype.xsd.datetime.IDateTimeValueType;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;
import de.fzi.dbs.verification.addon.datatype.ComparatorVC;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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

  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    final BigDateTimeValueType value = ((IDateTimeValueType) object).getBigValue();
    final BigInteger year = value.getYear();
    final Integer month = value.getMonth();
    final Integer day = value.getDay();
    final Integer hour = value.getHour();
    final Integer minute = value.getMinute();
    final BigDecimal second = value.getSecond();
    final TimeZone timeZone = value.getTimeZone();

    return JExpr._new(codeModel.ref(BigDateTimeValueType.class)).
      arg(null == year ? JExpr._null() : JExpr._new(codeModel.ref(BigInteger.class)).arg(JExpr.lit(year.toString()))).
      arg(null == month ? JExpr._null() : JExpr._new(codeModel.ref(Integer.class)).arg(JExpr.lit(month.intValue()))).
      arg(null == day ? JExpr._null() : JExpr._new(codeModel.ref(Integer.class)).arg(JExpr.lit(day.intValue()))).
      arg(null == hour ? JExpr._null() : JExpr._new(codeModel.ref(Integer.class)).arg(JExpr.lit(hour.intValue()))).
      arg(null == minute ? JExpr._null() : JExpr._new(codeModel.ref(Integer.class)).arg(JExpr.lit(minute.intValue()))).
      arg(null == second ? JExpr._null() : JExpr._new(codeModel.ref(BigDecimal.class)).arg(JExpr.lit(second.toString()))).
      arg(null == timeZone ? JExpr._null() :
      JExpr._new(codeModel.ref(SimpleTimeZone.class)).arg(JExpr.lit(timeZone.getRawOffset())).arg(null == timeZone.getID() ? JExpr._null() : JExpr.lit(timeZone.getID()))).invoke("toCalendar");
  }
}
