package tests.one.src.de.fzi.dbs.verification.tests.one;

import de.fzi.dbs.verification.ObjectVerifier;
import de.fzi.dbs.verification.tests.one.ObjectVerifierFactory;
import de.fzi.dbs.verification.tests.one.Root;
import de.fzi.dbs.verification.tests.one.impl.RootImpl;
import de.fzi.dbs.verification.tests.one.impl.ElementWithListAttribute1Impl;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * @author Aleksei Valikov
 */
public class VerificationTest extends TestCase
{
  /**
   * Logger.
   */
  protected Log log = LogFactory.getLog(tests.one.src.de.fzi.dbs.verification.tests.one.VerificationTest.class);
  /**
   * Object verifier factory.
   */
  protected ObjectVerifierFactory objectVerifierFactory;

  /**
   * Object verifier.
   */
  protected ObjectVerifier objectVerifier;

  /**
   * Validation event handler.
   */
  protected ValidationEventHandler handler;

  /**
   * Constructs a new test case.
   * 
   * @param name test name.
   */
  public VerificationTest(final String name)
  {
    super(name);
  }

  /**
   * Test setup.
   * 
   * @throws Exception In case of setup problems.
   */
  public void setUp() throws Exception
  {
    super.setUp();
    // Get an object verifier factory instance
    objectVerifierFactory = new ObjectVerifierFactory();
    // Get an object verifier instance
    objectVerifier = objectVerifierFactory.newInstance(Root.class);
    handler = new ValidationEventHandler()
    {
      public boolean handleEvent(ValidationEvent event)
      {
        log.debug(event.getMessage());
        return true;
      }
    };
  }

  public void testVerification()
  {
    final Root root = new RootImpl();
    root.setElementWithListAttribute1(new ElementWithListAttribute1Impl());
    root.getElementWithListAttribute1().getList1().add("abcd");
    objectVerifier.check(handler, root);
  }
}
