package de.fzi.dbs.verification.addon.datatype;

import com.sun.codemodel.JBlock;

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
}
