package de.fzi.dbs.verification.addon.datatype;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.UnionType;
import com.sun.msv.datatype.xsd.XSDatatype;

/**
 * Union type VC.
 *
 * @author Aleksei Valikov
 */
public class UnionTypeVC extends AbstractVC
{
  public JStatement verify(final DatabindableDatatype datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final UnionType unionType = (UnionType) datatype;

    final JBlock block = newBlock();
    final JBlock currentBlock = block;

    for (int index = 0; index < unionType.memberTypes.length; index++)
    {
      currentBlock.directStatement("// Or ...");
      final JVar currentProblem = currentBlock.decl(codeModel.ref(Object.class), "problem" + index);
      final XSDatatype currentDatatype = unionType.memberTypes[index];
      final VerificatorConstructor vc =
        VerificatorConstructorFactory.getVerificatorConstructor(currentDatatype);
      vc.verify(currentDatatype, codeModel, theClass, value, currentProblem);
      currentBlock.directStatement("// Add current problem to the list of problems");
    }
    block.directStatement("// todo: Throw \"object does not match any of the types of the union expression\"");
    return block;
  }

}
