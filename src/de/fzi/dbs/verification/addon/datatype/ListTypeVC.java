package de.fzi.dbs.verification.addon.datatype;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.ListType;

/**
 * List VC.
 */
public class ListTypeVC extends AbstractVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final ListType listType = (ListType) datatype;
    final VerificatorConstructor vc = VerificatorConstructorFactory.getVerificatorConstructor(listType.itemType);
    final JBlock block = newBlock();

    final JForLoop _for = block._for();

    final JVar index = _for.init(codeModel.INT, "index", JExpr.lit(0));
    _for.test(JOp.lt(index, value.ref("length")));
    _for.update(JOp.incr(index));

    final JVar current = _for.body().decl(codeModel.ref(listType.itemType.getJavaObjectType()),
      "current", value.component(index));
    final JVar currentProblem = _for.body().decl(codeModel.ref(Object.class), "currentProblem");
    _for.body().add(vc.verify(listType.itemType, codeModel, theClass, current, currentProblem));

    _for.body().directStatement("// Add current problem to the list of problems");
    return block;
  }
}
