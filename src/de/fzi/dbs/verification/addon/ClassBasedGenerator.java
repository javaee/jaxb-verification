package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.generator.ClassContext;

/**
 * Class-based generator stores code model ({@link #codeModel}) and context ({@link #classContext})
 * of the current class.
 *
 * @author Aleksei Valikov
 */
public abstract class ClassBasedGenerator extends Generator
{
  /**
   * Code model.
   */
  protected JCodeModel codeModel;

  /**
   * Class context.
   */
  protected ClassContext classContext;

  /**
   * Constructs a new class-based generated for the given class context.
   *
   * @param classContext class context.
   */
  protected ClassBasedGenerator(final ClassContext classContext)
  {
    this.codeModel = classContext.ref.owner();
    this.classContext = classContext;
  }


}
