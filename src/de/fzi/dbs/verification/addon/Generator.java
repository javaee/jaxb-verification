package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;

/**
 * Base class for generators.
 *
 * @author Aleksei Valikov
 */
public abstract class Generator
{
  /**
   * Generated class.
   */
  protected JDefinedClass theClass;

  /**
   * Generation template. Generates class {@link #generateClass()}, fields {@link #generateFields()}
   * and methods {@link #generateMethods()}.
   */
  public void generate()
  {
    theClass = generateClass();
    generateFields();
    generateMethods();
  }

  /**
   * Generates a new class or returns an existing class if generation failed due to the
   * {@link JClassAlreadyExistsException} exception.
   *
   * @return generated class.
   */
  protected JDefinedClass generateClass()
  {
    try
    {
      return generateClassInternal();
    }
    catch (JClassAlreadyExistsException jcaeex)
    {
      return jcaeex.getExistingClass();
    }
  }

  /**
   * Internal method for class generation.
   *
   * @return Generated class.
   * @throws JClassAlreadyExistsException Thrown if class already exists.
   */
  protected abstract JDefinedClass generateClassInternal() throws JClassAlreadyExistsException;

  /**
   * Generates fields.
   */
  protected abstract void generateFields();

  /**
   * Generates methods.
   */
  protected abstract void generateMethods();

  /**
   * Returns the generated class. Should only be called after {@link #generate()} method.
   *
   * @return Generated class.
   */
  public JDefinedClass getGeneratedClass()
  {
    return theClass;
  }
}
