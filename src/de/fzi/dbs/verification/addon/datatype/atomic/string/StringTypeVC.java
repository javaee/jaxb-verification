package de.fzi.dbs.verification.addon.datatype.atomic.string;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;
import de.fzi.dbs.verification.addon.datatype.DiscreteVC;

/**
 * VC for string type.
 *
 * @author Aleksei Valikov
 */
public class StringTypeVC extends AbstractVC implements DiscreteVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    return block;
  }

  public JExpression countLength(final JCodeModel codeModel, final JExpression value)
  {
    return JOp.cond(JOp.eq(JExpr._null(), value), JExpr.lit(0), JExpr.invoke(value, "length"));
  }
}
