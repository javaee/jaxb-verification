package de.fzi.dbs.jaxb.equality.addon;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JWhileLoop;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.FieldUse;
import com.sun.tools.xjc.grammar.ClassItem;
import de.fzi.dbs.jaxb.addon.Util;
import de.fzi.dbs.verification.addon.ClassBasedGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

/**
 * Generates {@link Object#equals(Object)} method.
 */
public class EqualsGenerator extends ClassBasedGenerator {

  /**
   * Constructs a new generator.
   *
   * @param classContext class context.
   */
  public EqualsGenerator(final ClassContext classContext) {
    super(classContext);
  }

  /**
   * Generated method.
   */
  protected JMethod equals;

  protected JDefinedClass generateClassInternal() throws JClassAlreadyExistsException {
    return null;
  }

  protected void generateFields() {
  }

  protected void generateMethods() {
    equals = generateEquals();
  }

  /**
   * Generates {@link Object#equals(Object)} method.
   *
   * @return Generated method.
   */
  protected JMethod generateEquals() {
    final JMethod equals = classContext.implClass.method(JMod.PUBLIC, codeModel.BOOLEAN, "equals");
    final JVar obj = equals.param(Object.class, "obj");

    final JConditional ifThisEqObj = equals.body()._if(JOp.eq(JExpr._this(), obj));
    ifThisEqObj._then()._return(JExpr.TRUE);

    final JConditional ifWrongObj = equals.body()._if(JOp.cor(JOp.eq(JExpr._null(), obj),
        JOp.ne(obj.invoke("getClass"), JExpr._this().invoke("getClass"))));

    ifWrongObj._then()._return(JExpr.FALSE);
    final JVar target = equals.body().decl(classContext.implClass, "target", JExpr.cast(classContext.implClass, obj));

    for (ClassItem item = classContext.target; item != null; item = item.getSuperClass()) {
      final ClassContext context = classContext.parent.getClassContext(item);
      generateFieldsEquality(context, equals.body(), target);
    }

    equals.body()._return(JExpr.TRUE);
    return equals;
  }

  /**
   * Generates field equality check instructions.
   *
   * @param classContext class context.
   * @param methodBody   method body.
   * @param target       target variable.
   */
  protected void generateFieldsEquality(final ClassContext classContext, final JBlock methodBody, final JVar target) {
    final FieldUse[] fieldUses = classContext.target.getDeclaredFieldUses();
    // Iterate over field uses
    for (int index = 0; index < fieldUses.length; index++) {
      final FieldUse fieldUse = fieldUses[index];
      final JMethod getter = Util.getFieldClassGetter(classContext, fieldUse);
      if (getter != null) {
        final JBlock block = methodBody.block();
        final JVar value = block.decl(getter.type(), "value", JExpr._this().invoke(getter));
        final JVar targetValue = block.decl(getter.type(), "targetValue", target.invoke(getter));
        valuesEqual(block, value, targetValue);
      }
    }
  }

  /**
   * Generates expression checking equality of two values.
   *
   * @param value       value.
   * @param targetValue target value.
   */
  protected void valuesEqual(final JBlock block, final JVar value, final JVar targetValue) {

    if (List.class.getName().equals(value.type().fullName())) {

      final JConditional _if =
          block._if(JOp.ne(value, targetValue));

      _if._then()._if(JOp.cor(JOp.eq(value, JExpr._null()), JOp.eq(targetValue, JExpr._null())))._then()._return(JExpr.FALSE);

      final JVar e1 = _if._then().decl(codeModel.ref(Iterator.class), "e1", value.invoke("iterator"));
      final JVar e2 = _if._then().decl(codeModel.ref(Iterator.class), "e2", targetValue.invoke("iterator"));
      {
        final JWhileLoop _while = _if._then()._while(JOp.cand(e1.invoke("hasNext"), e2.invoke("hasNext")));
        final JVar o1 = _while.body().decl(codeModel.ref(Object.class), "o1", e1.invoke("next"));
        final JVar o2 = _while.body().decl(codeModel.ref(Object.class), "o2", e2.invoke("next"));

        _while.body().
            _if(JOp.not(JOp.cond(JOp.eq(o1, JExpr._null()), JOp.eq(o2, JExpr._null()), o1.invoke("equals").arg(o2))))._then()._return(JExpr.FALSE);
      }
      _if._then()._if(JOp.cor(e1.invoke("hasNext"), e2.invoke("hasNext")))._then()._return(JExpr.FALSE);
    }
    else if (value.type().isArray()) {
      final JConditional ifValuesNotEqual = block._if(JOp.not(codeModel.ref(Arrays.class).staticInvoke("equals").arg(value).arg(targetValue)));
      ifValuesNotEqual._then()._return(JExpr.FALSE);
    }
    else if (value.type().isPrimitive()) {
      final JConditional ifValuesNotEqual = block._if(JOp.ne(value, targetValue));
      ifValuesNotEqual._then()._return(JExpr.FALSE);
    }
    else {
      final JConditional ifValuesNotEqual = block._if(JOp.not(JOp.cor(JOp.eq(value, targetValue),
          JOp.cand(JOp.ne(value, JExpr._null()),
              value.invoke("equals").arg(targetValue)))));
      ifValuesNotEqual._then()._return(JExpr.FALSE);
    }
  }

}
