package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.msv.grammar.AttributeExp;
import com.sun.msv.grammar.ChoiceExp;
import com.sun.msv.grammar.ConcurExp;
import com.sun.msv.grammar.DataExp;
import com.sun.msv.grammar.ElementExp;
import com.sun.msv.grammar.Expression;
import com.sun.msv.grammar.InterleaveExp;
import com.sun.msv.grammar.ListExp;
import com.sun.msv.grammar.MixedExp;
import com.sun.msv.grammar.OneOrMoreExp;
import com.sun.msv.grammar.ReferenceExp;
import com.sun.msv.grammar.SequenceExp;
import com.sun.msv.grammar.ValueExp;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.ExternalItem;
import com.sun.tools.xjc.grammar.FieldItem;
import com.sun.tools.xjc.grammar.IgnoreItem;
import com.sun.tools.xjc.grammar.InterfaceItem;
import com.sun.tools.xjc.grammar.PrimitiveItem;
import com.sun.tools.xjc.grammar.SuperClassItem;
import de.fzi.dbs.jaxb.addon.Util;

/**
 * This visitors generates "existence" predicates - predicates, turning into true if and only
 * if object fulfills the condition of expression existance.
 *
 * @author Aleksei Valikov
 */
public class ExistsVisitor extends BGMExpressionVisitor
{
  /**
   * Code model.
   */
  protected final JCodeModel codeModel;

  /**
   * Class context.
   */
  protected final ClassContext classContext;


  /**
   * Master object.
   */
  protected final JExpression master;

  /**
   * Constructs a nee existance visitor.
   *
   * @param classContext class context.
   * @param master       master object.
   */
  public ExistsVisitor(final ClassContext classContext, final JExpression master)
  {
    this.classContext = classContext;
    codeModel = classContext.parent.getCodeModel();
    this.master = master;
  }

  public Object onField(final FieldItem item)
  {
    // todo: Use isSet methods to detect if field was set.
    final JMethod getter = Util.getFieldInterfaceGetter(classContext, item);
    final JType type = item.getType(codeModel);
    if (type.isPrimitive())
    {
      // We can't check if primitive value was set or not, so we'll assume it always is set.
      return JExpr.TRUE;
    }
    else
    {
      final JExpression notNull = JOp.ne(JExpr._null(), JExpr.invoke(master, getter));
      if (!item.multiplicity.isAtMostOnce())
      {
        return JOp.cand(notNull, JOp.gt(JExpr.invoke(JExpr.invoke(master, getter), "size"), JExpr.lit(0)));
      }
      else
      {
        return notNull;
      }
    }
  }

  public Object onClass(final ClassItem item)
  {
    return null;
  }

  public Object onExternal(final ExternalItem item)
  {
    return null;
  }

  public Object onIgnore(final IgnoreItem item)
  {
    return null;
  }

  public Object onInterface(final InterfaceItem item)
  {
    return null;
  }

  public Object onPrimitive(final PrimitiveItem item)
  {
    return null;
  }

  public Object onSuper(final SuperClassItem item)
  {
    return null;
  }

  public Object onAnyString()
  {
    return null;
  }

  public Object onEpsilon()
  {
    return JExpr.TRUE;
  }

  public Object onNullSet()
  {
    return null;
  }

  public Object onAttribute(final AttributeExp exp)
  {
    return null;
  }


  public Object onConcur(final ConcurExp p)
  {
    return null;
  }

  public Object onData(final DataExp exp)
  {
    return null;
  }

  public Object onElement(final ElementExp exp)
  {
    return JExpr.TRUE;
  }

  public Object onInterleave(final InterleaveExp p)
  {
    return null;
  }

  public Object onList(final ListExp exp)
  {
    return null;
  }

  public Object onMixed(final MixedExp exp)
  {
    return null;
  }

  public Object onRef(final ReferenceExp exp)
  {
    return null;
  }

  public Object onSequence(final SequenceExp exp)
  {
    final JExpression left = (JExpression) exp.exp1.visit(this);
    final JExpression right = (JExpression) exp.exp2.visit(this);
    if (left == JExpr.TRUE)
    {
      if (right == JExpr.TRUE)
      {
        return JExpr.TRUE;
      }
      else
      {
        return right;
      }
    }
    else if (right == JExpr.TRUE)
    {
      return left;
    }
    else
    {
      return JOp.cand(left, right);
    }
  }

  public Object onChoice(final ChoiceExp exp)
  {
    if (exp.exp1 == Expression.epsilon || exp.exp2 == Expression.epsilon)
    {
      return JExpr.TRUE;
    }
    else
    {
      final JExpression left = (JExpression) exp.exp1.visit(this);
      final JExpression right = (JExpression) exp.exp2.visit(this);

      if (left == JExpr.TRUE)
      {
        if (right == JExpr.FALSE)
        {
          return JExpr.TRUE;
        }
        else if (right == JExpr.TRUE)
        {
          return JExpr.FALSE;
        }
        else
        {
          return JOp.not(right);
        }
      }
      if (left == JExpr.FALSE)
      {
        if (right == JExpr.FALSE)
        {
          return JExpr.FALSE;
        }
        else if (right == JExpr.TRUE)
        {
          return JExpr.TRUE;
        }
        else
        {
          return right;
        }
      }
      else if (right == JExpr.FALSE)
      {
        return left;
      }
      else if (right == JExpr.TRUE)
      {
        return JOp.not(left);
      }
      else
      {
        return JOp.cor(JOp.cand(left, JOp.not(right)), JOp.cand(JOp.not(left), right));
      }
    }
  }

  public Object onOneOrMore(final OneOrMoreExp exp)
  {
    return exp.exp.visit(this);
  }

  public Object onValue(final ValueExp exp)
  {
    return null;
  }
}
