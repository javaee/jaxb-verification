package de.fzi.dbs.verification.addon;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.CodeAugmenter;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.generator.GeneratorContext;
import com.sun.tools.xjc.grammar.AnnotatedGrammar;
import com.sun.tools.xjc.grammar.ClassItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;

import java.io.IOException;

/**
 * Add-on for generating verification routines.
 *
 * @author Aleksei Valikov
 */
public class AddOn implements CodeAugmenter
{
  /**
   * Logger.
   */
  protected Log log = LogFactory.getLog(AddOn.class);

  /**
   * Does nothing.
   *
   * @param opt  ignored.
   * @param args ignored.
   * @param i    ignored.
   * @return Alway returns <code>0</code>.
   * @throws com.sun.tools.xjc.BadCommandLineException
   *                             Never thrown.
   * @throws java.io.IOException Never thrown.
   */
  public int parseArgument(final Options opt, final String[] args, final int i)
    throws BadCommandLineException, IOException
  {
    return 0;   // no option recognized
  }

  /**
   * Augments the code.
   *
   * @param grammar          annotated grammar.
   * @param generatorContext generator context.
   * @param options          options.
   * @param errorHandler     error handler.
   * @return If the add-on executes successfully, return true. If it detects some errors but those are reported and
   *         recovered gracefully, return false.
   */
  public boolean run(final AnnotatedGrammar grammar, final GeneratorContext generatorContext,
    final Options options, final ErrorHandler errorHandler)
  {

    // Augment code
    {
      log.debug("Starting code augmentation.");

      // Process class items
      final ClassItem[] classItems = grammar.getClasses();
      log.debug("Processing [" + classItems.length + "] class items.");
      for (int index = 0; index < classItems.length; index++)
      {
        final ClassItem classItem = classItems[index];
        final ClassContext classContext = generatorContext.getClassContext(classItem);
        processClass(classContext, classItem);
      }
      log.debug("Finished processing class items.");
    }

    log.debug("Finished code augmentation.");
    return true;
  }

  public String getOptionName()
  {
    return "Xverification";
  }

  public String getUsage()
  {
    return "  -Xverification               :  build object verifiers";
  }

  /**
   * Processes the class.
   *
   * @param classContext class context.
   * @param classItem    class item.
   */
  protected void processClass(final ClassContext classContext, final ClassItem classItem)
  {
    final VerifierGenerator verifierGenerator = new VerifierGenerator(classContext);
    verifierGenerator.generate();
    final JDefinedClass objectVerifier = verifierGenerator.getGeneratedClass();
    final JDefinedClass objectVerifierFactory = getOrGenerateObjectVerifierFactoryClass(classContext.ref._package());
    objectVerifierFactory.init().
      invoke(JExpr.ref("objectVerifierClasses"), "put").arg(classContext.ref.staticRef("class")).arg(objectVerifier.staticRef("class"));
    objectVerifierFactory.init().
      invoke(JExpr.ref("objectVerifierClasses"), "put").arg(classContext.implClass.staticRef("class")).arg(objectVerifier.staticRef("class"));
  }

  /**
   * Creates or returnes existing object verifier factory class for given package.
   *
   * @param thePackage package to return object verifier factory for.
   * @return Object verifier factory.
   */
  public static JDefinedClass getOrGenerateObjectVerifierFactoryClass(final JPackage thePackage)
  {
    try
    {
      final JDefinedClass objectVerifierFactory = thePackage._class(JMod.PUBLIC, "ObjectVerifierFactory");
      objectVerifierFactory._extends(de.fzi.dbs.verification.ObjectVerifierFactory.class);
      return objectVerifierFactory;
    }
    catch (JClassAlreadyExistsException jcaeex)
    {
      return jcaeex.getExistingClass();
    }
  }
}
