package de.fzi.dbs.verification.addon.datatype;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JCodeModel;

/**
 * Abstract verification constructor. Provides an utility {@link #newBlock} method.
 */
public abstract class AbstractVC implements VerificatorConstructor
{
  /**
   * Returns a new empty block.
   * 
   * @return New empty block.
   */
  protected JBlock newBlock()
  {
    return JBlock.dummyInstance.block();
  }

//  public JExpression create(Object object, JCodeModel codeModel)
//  {
//    return null;
//  }
}
