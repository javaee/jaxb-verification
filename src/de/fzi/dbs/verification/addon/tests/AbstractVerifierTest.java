package de.fzi.dbs.verification.addon.tests;

import de.fzi.dbs.verification.ObjectVerifier;
import de.fzi.dbs.verification.ObjectVerifierFactory;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.MockControl;

import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * Abstract base class for verifier tests. Implementations must implement
 * {@link #getObjects()} method to return array of objects to be validated and
 * {@link #getEvents()} method to return array of event lists that must be reported.
 * The {@link #testValidation()} method checks that validation of each of the objects
 * reports the expected events.
 *
 * @author Aleksei Valikov
 */
public abstract class AbstractVerifierTest extends TestCase
{
  /**
   * Logger.
   */
  protected Log log = LogFactory.getLog(AbstractVerifierTest.class);

  /**
   * Constructs a new test case.
   * 
   * @param name test name.
   */
  public AbstractVerifierTest(final String name)
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
  }

  /**
   * Performs validation tests. This method iterates over the {@link #getObjects()},
   * validates each of the items and checks all the predefined problems are reported.
   */
  public void testValidation()
  {
    final ObjectVerifierFactory objectVerifierFactory = getObjectVerifierFactory();
    final Object[] objects = getObjects();
    final ValidationEvent[][] events = getEvents();
    Assert.assertEquals("Number of objects and validation events must be equal.", objects.length, events.length);
    for (int index = 0; index < objects.length; index++)
    {
      final Object object = objects[index];
      final MockControl mockControl = MockControl.createStrictControl(ValidationEventHandler.class);
      //mockControl.setDefaultMatcher(MockControl.EQUALS_MATCHER);
      final ValidationEventHandler handler = (ValidationEventHandler) mockControl.getMock();
      for (int eventIndex = 0; eventIndex < events[index].length; eventIndex++)
      {
        handler.handleEvent(events[index][eventIndex]);
        mockControl.setReturnValue(true);
      }
      mockControl.replay();
      try
      {
        final ObjectVerifier objectVerifier = objectVerifierFactory.newInstance(object.getClass());
        objectVerifier.check(handler, object);
      }
      catch (JAXBException jaxbex)
      {
        log.error("Unexpected exception.", jaxbex);
        Assert.fail("Unexpected exception.");
      }
      mockControl.verify();
    }
  }

  /**
   * Returns objects that will be validated.
   *
   * @return Objects that will be validated.
   */
  protected abstract Object[] getObjects();

  /**
   * Returns arrays validation expected validation events.
   *
   * @return Arrays validation expected validation events.
   */
  protected abstract ValidationEvent[][] getEvents();

  /**
   * Returns object verifier factory to be tested.
   *
   * @return Object verifier factory that will be tested.
   */
  protected abstract ObjectVerifierFactory getObjectVerifierFactory();
}
