package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JDefinedClass;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import de.fzi.dbs.verification.addon.datatype.DiscreteVC;
import de.fzi.dbs.verification.addon.datatype.VerificatorConstructor;
import de.fzi.dbs.verification.addon.datatype.VerificatorConstructorFactory;

/**
 * Base VC for length facets.
 *
 * @author Aleksei Valikov
 */
public abstract class BaseLengthFacetVC extends DataTypeWithValueConstraintFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final VerificatorConstructor vc = VerificatorConstructorFactory.getVerificatorConstructor(datatype.getConcreteType());
    final JExpression length = ((DiscreteVC) vc).countLength(codeModel, value);
    final JExpression expectedLength = JExpr.lit(expectedLength(datatype));
    final JConditional ifValueLengthIsCorrect = block._if(lengthValid(codeModel, length, expectedLength));
    ifValueLengthIsCorrect._then().directStatement("// Value length is correct");
    ifValueLengthIsCorrect._else().
      assign(problem, JExpr._new(problemClass(codeModel)).arg(value).arg(length).arg(expectedLength));
    return block;
  }

  /**
   * Returns expected length parameter. Subclasses must implement this method to retrieve expected
   * length from the datatype.
   *
   * @param datatype datatype.
   * @return Expected length.
   */
  public abstract int expectedLength(DataTypeWithFacet datatype);

  /**
   * Returns class of the problem.
   *
   * @param codeModel code model.
   * @return Class of the problem.
   */
  public abstract JClass problemClass(JCodeModel codeModel);

  /**
   * Returns an expression that compares length and expected length. Expression should return <code>true</code>
   * if length is correct.
   *
   * @param codeModel      code model.
   * @param length         effective length.
   * @param expectedLength expected length.
   * @return Length comparison expression.
   */
  public abstract JExpression lengthValid(JCodeModel codeModel, JExpression length, JExpression expectedLength);
}
