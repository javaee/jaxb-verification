package de.fzi.dbs.verification.addon.datatype.atomic;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.msv.datatype.DatabindableDatatype;
import de.fzi.dbs.verification.addon.datatype.AbstractVC;

/**
 * VC for entity class.
 *
 * @author Aleksei Valikov
 */
public class EntityTypeVC extends AbstractVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    block.directStatement("// todo: validate entity type. How?");
    return block;
  }

  public JExpression create(final DatabindableDatatype datatype, final JCodeModel codeModel, final Object object)
  {
    return JExpr.lit(object.toString());
  }
}
