package de.fzi.dbs.jaxb.equality.addon;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.FieldUse;
import com.sun.tools.xjc.grammar.ClassItem;
import de.fzi.dbs.jaxb.addon.Util;
import de.fzi.dbs.verification.addon.ClassBasedGenerator;

/**
 * Generates {@link Object#hashCode()} method.
 */
public class HashCodeGenerator extends ClassBasedGenerator {

  /**
   * Constructs a new generator.
   * @param classContext class context.
   */
  public HashCodeGenerator(final ClassContext classContext) {
    super(classContext);
  }

  /**
   * Generated method.
   */
  protected JMethod hashCode;

  protected JDefinedClass generateClassInternal() throws JClassAlreadyExistsException {
    return null;
  }

  protected void generateFields() {
  }

  protected void generateMethods() {
    hashCode = generateHashCode();
  }

  /**
   * Generates {@link Object#hashCode()} method.
   * @return Generated method.
   */
  protected JMethod generateHashCode() {
    final JMethod hashCode = classContext.implClass.method(JMod.PUBLIC, codeModel.INT, "hashCode");
    final JVar hash = hashCode.body().decl(codeModel.INT, "hash", JExpr.lit(7));

    for (ClassItem item = classContext.target; item != null; item = item.getSuperClass()) {
      final ClassContext context = classContext.parent.getClassContext(item);
      generateFieldsHashCode(context, hashCode.body(), hash);
    }

    hashCode.body()._return(hash);
    return hashCode;
  }

  /**
   * Generates fields hashcode instructions.
   * @param classContext class context.
   * @param methodBody method body.
   * @param hash hash variable.
   */
  protected void generateFieldsHashCode(final ClassContext classContext, final JBlock methodBody, final JVar hash) {
    final FieldUse[] fieldUses = classContext.target.getDeclaredFieldUses();
    for (int index = 0; index < fieldUses.length; index++) {
      final FieldUse fieldUse = fieldUses[index];
      final JMethod getter = Util.getFieldClassGetter(classContext, fieldUse);
      if (getter != null) {
        final JBlock block = methodBody.block();
        final JVar value = block.decl(getter.type(), "value", JExpr._this().invoke(getter));

        if (value.type().isArray()) {

          final JForLoop _for = block._for();
          final JVar i = _for.init(codeModel.INT, "i", JExpr.lit(0));
          _for.test(JOp.lt(i, value.ref("length")));
          _for.update(JOp.incr(i));
          final JVar valueComponent =
              _for.body().decl(value.type().elementType(), "valueComponent", value.component(i));
          _for.body().assign(hash, JOp.plus(JOp.mul(JExpr.lit(31), hash), valueHashCode(valueComponent)));
        }
        else {
          block.assign(hash, JOp.plus(JOp.mul(JExpr.lit(31), hash), valueHashCode(value)));
        }
      }
    }
  }

  /**
   * Generates hash code expression for value.
   * @param value value.
   * @return Hash code expression for the value.
   */
  protected JExpression valueHashCode(final JVar value) {

    if ("byte".equals(value.type().fullName())) {
      return JExpr.cast(codeModel.INT, value);
    }
    else if ("short".equals(value.type().fullName())) {
      return JExpr.cast(codeModel.INT, value);
    }
    else if ("boolean".equals(value.type().fullName())) {
      return JOp.cond(value, JExpr.lit(1), JExpr.lit(0));
    }
    else if ("char".equals(value.type().fullName())) {
      return JExpr.cast(codeModel.INT, value);
    }
    else if ("int".equals(value.type().fullName())) {
      return value;
    }
    else if ("long".equals(value.type().fullName())) {
      return JExpr.cast(codeModel.INT, JOp.xor(value, JOp.shrz(value, JExpr.lit(32))));
    }
    else if ("float".equals(value.type().fullName())) {
      return codeModel.ref(Float.class).staticInvoke("floatToIntBits").arg(value);
    }
    else if ("double".equals(value.type().fullName())) {
      return JExpr.cast(codeModel.INT, JOp.xor(codeModel.ref(Double.class).staticInvoke("doubleToLongBits").arg(value), JOp.shrz(codeModel.ref(Double.class).staticInvoke("doubleToLongBits").arg(value), JExpr.lit(32))));
    }
    else {
      return JOp.cond(JOp.eq(JExpr._null(), value), JExpr.lit(0), value.invoke("hashCode"));
    }
  }
}
