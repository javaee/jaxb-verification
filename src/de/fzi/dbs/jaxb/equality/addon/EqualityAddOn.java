package de.fzi.dbs.jaxb.equality.addon;

import com.sun.tools.xjc.CodeAugmenter;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.generator.GeneratorContext;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.grammar.AnnotatedGrammar;
import com.sun.tools.xjc.grammar.ClassItem;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;

import java.io.IOException;

/**
 * Add-on for {@link Object#equals(Object)} and {@link Object#hashCode()} methods generation.
 * @author Aleksei Valikov
 */
public class EqualityAddOn implements CodeAugmenter {

  /**
   * Logger.
   */
  protected Log log = LogFactory.getLog(EqualityAddOn.class);

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
      throws BadCommandLineException, IOException {
    return 0;   // no option recognized
  }

  public String getOptionName() {
    return "Xequality";
  }

  public String getUsage() {
    return "  -Xequality                   :  adds hashCode(...) and hashCode() methods";
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
                     final Options options, final ErrorHandler errorHandler) {

    log.debug("Starting code augmentation.");

    // Process class items
    final ClassItem[] classItems = grammar.getClasses();
    log.debug("Processing [" + classItems.length + "] class items.");
    for (int index = 0; index < classItems.length; index++) {
      final ClassItem classItem = classItems[index];
      final ClassContext classContext = generatorContext.getClassContext(classItem);
      log.debug("Processing [" + classItem.name + "].");

      final EqualsGenerator equalsGenerator = new EqualsGenerator(classContext);
      equalsGenerator.generate();

      final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator(classContext);
      hashCodeGenerator.generate();
    }
    log.debug("Finished processing class items.");
    log.debug("Finished code augmentation.");
    return true;
  }


}
