package de.fzi.dbs.verification.addon;

import com.sun.msv.grammar.ExpressionVisitor;
import com.sun.msv.grammar.OtherExp;
import com.sun.tools.xjc.grammar.JavaItem;
import com.sun.tools.xjc.grammar.JavaItemVisitor;

/**
 * BGM expression visitors implements both {@link JavaItemVisitor} and {@link ExpressionVisitor}
 * interfaces.
 *
 * @author Aleksei Valikov
 */
public abstract class BGMExpressionVisitor implements JavaItemVisitor, ExpressionVisitor
{
  /**
   * If expression is a {@link JavaItem}, invokes {@link JavaItem#visitJI(JavaItemVisitor)} with
   * this visitor.
   *
   * @param exp expression to visit.
   * @return Result of visiting.
   */
  public Object onOther(final OtherExp exp)
  {
    if (exp instanceof JavaItem)
    {
      return ((JavaItem) exp).visitJI(this);
    }
    else
    {
      return exp.exp.visit(this);
    }
  }
}
