package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JPrimitiveType;
import com.sun.codemodel.JStatement;
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
import com.sun.msv.grammar.OtherExp;
import com.sun.msv.grammar.ReferenceExp;
import com.sun.msv.grammar.SequenceExp;
import com.sun.msv.grammar.ValueExp;
import com.sun.msv.grammar.xmlschema.OccurrenceExp;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.ExternalItem;
import com.sun.tools.xjc.grammar.FieldItem;
import com.sun.tools.xjc.grammar.FieldUse;
import com.sun.tools.xjc.grammar.IgnoreItem;
import com.sun.tools.xjc.grammar.InterfaceItem;
import com.sun.tools.xjc.grammar.PrimitiveItem;
import com.sun.tools.xjc.grammar.SuperClassItem;
import de.fzi.dbs.jaxb.addon.Util;
import de.fzi.dbs.verification.event.VerificationEvent;
import de.fzi.dbs.verification.event.VerificationEventLocator;
import de.fzi.dbs.verification.event.structure.EmptyFieldProblem;
import de.fzi.dbs.verification.event.structure.TooFewElementsProblem;
import de.fzi.dbs.verification.event.structure.TooManyElementsProblem;

/**
 * This visitor generates statements that check structure of the master object according to the
 * visited expression.
 *
 * @author Aleksei Valikov
 */
public class CheckVisitor extends BGMExpressionVisitor
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
   * Event handler.
   */
  protected final JExpression handler;

  /**
   * Parent locator.
   */
  protected final JExpression parentLocator;

  /**
   * Master object.
   */
  protected final JExpression master;

  /**
   * Constructs a check visitor.
   *
   * @param classContext  class context.
   * @param handler       validation event handler.
   * @param parentLocator parent locator.
   * @param master        master object
   */
  public CheckVisitor(final ClassContext classContext, final JExpression handler, final JExpression parentLocator, final JExpression master)
  {
    this.classContext = classContext;
    codeModel = classContext.parent.getCodeModel();
    this.handler = handler;
    this.parentLocator = parentLocator;
    this.master = master;
  }

  /**
   * Visits an occurence expression.
   * @param exp the occurence expression.
   * @return Result of the processing.
   */
  public Object onOccurrenceExp(final OccurrenceExp exp)
  {
    // Simply visit the item expression.
    final JBlock block = (JBlock) exp.itemExp.visit(this);
    return block;
  }

  public Object onOther(final OtherExp exp)
  {
    if (exp instanceof OccurrenceExp)
    {
      return onOccurrenceExp((OccurrenceExp) exp);
    }
    else
    {
      return super.onOther(exp);
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

  public Object onField(final FieldItem fieldItem)
  {
    final JBlock block = JBlock.dummyInstance.block();

//    final JExpression locator = JExpr._new(classContext.parent.getCodeModel().ref(VerificationEventLocator.class)).arg(this.parentLocator).arg(master).arg(JExpr.lit(fieldItem.name));

    final FieldUse fieldUse = classContext.target.getField(fieldItem.name);

    final JMethod getter = Util.getFieldInterfaceGetter(classContext, fieldItem);

    JBlock checkBlock = block;
    // If field is not a constant field
    if (null != getter)
    {
      final JExpression field = JExpr.invoke(master, getter);

      if (fieldUse.type.isPrimitive())
      {
        block.directStatement("// No check for primitive values");
      }
      else
      {
        /// if (null == master.get<Field>()
        final JConditional ifNullConditional = block._if(JOp.eq(JExpr._null(), field));
        final JBlock ifNull = ifNullConditional._then();
        final JBlock ifNotNull = ifNullConditional._else();

        // A, A?
        if (fieldUse.multiplicity.isAtMostOnce())
        {
          if (fieldUse.multiplicity.isOptional())
          {
            ifNull.directStatement("// Optional field - nothing to report");
          }
          else
          {
            ifNull.directStatement("// Report missing object");
            ifNull.invoke(handler, "handleEvent").arg(JExpr._new(codeModel.ref(VerificationEvent.class)).
              arg(JExpr._new(codeModel.ref(VerificationEventLocator.class)).arg(parentLocator).arg(master).arg(JExpr.lit(fieldUse.name))).
              arg(JExpr._new(codeModel.ref(EmptyFieldProblem.class))));
          }
        }
        // A*, A+
        else
        {
          if (fieldItem.multiplicity.isOptional())
          {
            ifNull.directStatement("// Optional - nothing to report");
          }
          else
          {
            ifNull.directStatement("// Report missing object");
            ifNull.invoke(handler, "handleEvent").arg(JExpr._new(codeModel.ref(VerificationEvent.class)).
              arg(JExpr._new(codeModel.ref(VerificationEventLocator.class)).arg(parentLocator).arg(master).arg(JExpr.lit(fieldUse.name))).
              arg(JExpr._new(codeModel.ref(EmptyFieldProblem.class))));
          }
          ifNotNull.directStatement("// Check count");
          final JExpression count = JExpr.invoke(field, "size");
          final JExpression minCount = JExpr.lit(fieldUse.multiplicity.min);

          /// if (getField().size() < minOccurs)
          final JBlock minOccursViolated = ifNotNull.
            _if(JOp.lt(count, minCount))._then();

          minOccursViolated.directStatement("// Report minimum of occurences violated");

          minOccursViolated.invoke(handler, "handleEvent").arg(JExpr._new(codeModel.ref(VerificationEvent.class)).
            arg(JExpr._new(codeModel.ref(VerificationEventLocator.class)).
            arg(parentLocator).
            arg(master).
            arg(JExpr.lit(fieldUse.name))).
            arg(JExpr._new(codeModel.ref(TooFewElementsProblem.class)).arg(count).arg(minCount)));

          // If maxOccurs is not "unbounded"
          if (fieldUse.multiplicity.max != null)
          {
            final JExpression maxCount = JExpr.lit(fieldUse.multiplicity.max.intValue());
            /// if (getField().size() > maxOccurs)
            final JBlock maxOccursViolated = ifNotNull.
              _if(JOp.gt(count, JExpr.lit(fieldUse.multiplicity.max.intValue())))._then();

            maxOccursViolated.directStatement("// Report maximum of occurences violated");

            maxOccursViolated.invoke(handler, "handleEvent").arg(JExpr._new(codeModel.ref(VerificationEvent.class)).
              arg(JExpr._new(codeModel.ref(VerificationEventLocator.class)).
              arg(parentLocator).
              arg(master).
              arg(JExpr.lit(fieldUse.name))).
              arg(JExpr._new(codeModel.ref(TooManyElementsProblem.class)).arg(count).arg(maxCount)));
          }
        }
        ifNotNull.directStatement("// Check value");
        checkBlock = ifNotNull;
      }
      checkBlock.invoke("check" + fieldUse.name).arg(parentLocator).arg(handler).arg(master).
        arg((fieldUse.multiplicity.isAtMostOnce() && fieldUse.type.isPrimitive()) ?
        ((JPrimitiveType) fieldUse.type).wrap(field) : field);
    }
    return block;
  }

  public Object onIgnore(final IgnoreItem item)
  {
    final JBlock block = JBlock.dummyInstance.block();
    return block;
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
    final JBlock block = JBlock.dummyInstance.block();
    block.invoke(JExpr.ref("super"), "check").arg(parentLocator).arg(handler).arg(master);
    return block;
  }

  public Object onAnyString()
  {
    return null;
  }

  public Object onEpsilon()
  {
    final JBlock block = JBlock.dummyInstance.block();
    return block;
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
    final JBlock block = JBlock.dummyInstance.block();
    final JStatement statement = (JStatement) exp.contentModel.visit(this);
    block.add(statement);
    return block;
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

  public Object onOneOrMore(final OneOrMoreExp exp)
  {
    final JBlock block = (JBlock) exp.exp.visit(this);
    return block;
  }

  public Object onRef(final ReferenceExp exp)
  {
    return null;
  }

  public Object onSequence(final SequenceExp exp)
  {
    final JBlock block = JBlock.dummyInstance.block();
    final JBlock left = (JBlock) exp.exp1.visit(this);
    final JBlock right = (JBlock) exp.exp2.visit(this);
    block.add(left);
    block.add(right);
    return block;
  }

  public Object onChoice(final ChoiceExp exp)
  {
    final JBlock block = JBlock.dummyInstance.block();
    final JExpression leftExists = (JExpression) exp.exp1.visit(new ExistsVisitor(classContext, master));
    final JExpression rightExists = (JExpression) exp.exp2.visit(new ExistsVisitor(classContext, master));

    if (exp.exp2 == Expression.epsilon)
    {
      final JConditional l = block._if(leftExists);
      l._then().directStatement("// If left exists");
      l._then().add((JBlock) exp.exp1.visit(this));
      return block;
    }
    else if (exp.exp1 == Expression.epsilon)
    {
      final JConditional r = block._if(rightExists);
      r._then().directStatement("// If right exists");
      r._then().add((JBlock) exp.exp2.visit(this));
      return block;
    }
    else
    {
      block.directStatement("// Choice");
      final JConditional lnr = block._if(JOp.not(rightExists));
      lnr._then().directStatement("// If right does not exist, left must exist");
      lnr._then().add((JBlock) exp.exp1.visit(this));

      final JConditional nlr = block._if(JOp.not(leftExists));
      nlr._then().directStatement("// If left does not exist, right must exist");
      nlr._then().add((JBlock) exp.exp2.visit(this));

      final JConditional nlnr = block._if(leftExists);
      nlnr._then().directStatement("// If left exists, right must not exist");
      nlnr._then().add((JBlock) exp.exp1.visit(this));
      nlnr._then().directStatement("// todo: check that right is absent. how?");
    }
    return block;
  }


  public Object onValue(final ValueExp exp)
  {
    return null;
  }

}
