package de.fzi.dbs.verification.datatype.facet.value;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JAssignmentTarget;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import com.sun.msv.datatype.xsd.RangeFacet;
import de.fzi.dbs.verification.datatype.ComparatorVC;
import de.fzi.dbs.verification.datatype.VerificatorConstructor;
import de.fzi.dbs.verification.datatype.VerificatorConstructorFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Fange facet.
 *
 * @author Aleksei Valikov
 */
public abstract class RangeFacetVC extends DataTypeWithValueConstraintFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, final JExpression value, final JAssignmentTarget problem)
  {
    final RangeFacet rangeFacet = (RangeFacet) datatype;
    JExpression limitValue = null;

    // Maybe move this to an utility class ?
    final Object lv = rangeFacet.limitValue;
    if (lv instanceof BigDecimal)
    {
      limitValue = JExpr._new(codeModel.ref(BigDecimal.class)).arg(JExpr.lit(lv.toString()));
    }
    else if (lv instanceof BigInteger)
    {
      limitValue = JExpr._new(codeModel.ref(BigInteger.class)).arg(JExpr.lit(lv.toString()));
    }
    else if (lv instanceof Byte)
    {
      limitValue = JExpr._new(codeModel.ref(Byte.class)).arg(JExpr.lit(((Byte) lv).byteValue()));
    }
    else if (lv instanceof Double)
    {
      limitValue = JExpr._new(codeModel.ref(Double.class)).arg(JExpr.lit(((Double) lv).doubleValue()));
    }
    else if (lv instanceof Float)
    {
      limitValue = JExpr._new(codeModel.ref(Float.class)).arg(JExpr.lit(((Float) lv).floatValue()));
    }
    else if (lv instanceof Integer)
    {
      limitValue = JExpr._new(codeModel.ref(Integer.class)).arg(JExpr.lit(((Integer) lv).intValue()));
    }
    else if (lv instanceof Long)
    {
      limitValue = JExpr._new(codeModel.ref(Long.class)).arg(JExpr.lit(((Long) lv).longValue()));
    }
    else if (lv instanceof Short)
    {
      limitValue = JExpr._new(codeModel.ref(Short.class)).arg(JExpr.lit(((Short) lv).shortValue()));
    }

    // todo: java.util.Calendar
    // todo: Duration

    final JBlock block = newBlock();
    if (limitValue == null)
    {
      block.directStatement("// todo: could not get object for " + lv.getClass() + " datatype");
    }
    else
    {

      final VerificatorConstructor vc = VerificatorConstructorFactory.getVerificatorConstructor(rangeFacet.getConcreteType());
      final JExpression compare = ((ComparatorVC) vc).compare(codeModel, value, limitValue);

      final JConditional ifRangeIsValid = block._if(rangeValid(codeModel, compare));
      ifRangeIsValid._then().directStatement("// Range is valid");
      ifRangeIsValid._else().
        assign(problem, JExpr._new(problemClass(codeModel)).arg(value).arg(limitValue));
    }
    return block;
  }

  /**
   * Returns expression that checks range validity. This expression should return <code>true</code>
   * if range is valid.
   *
   * @param codeModel code model.
   * @param range     range.
   * @return Range validity expression.
   */
  public abstract JExpression rangeValid(JCodeModel codeModel, JExpression range);

  /**
   * Returns class of the problem.
   *
   * @param codeModel code model.
   * @return Class of the problem.
   */
  public abstract JClass problemClass(JCodeModel codeModel);
}
