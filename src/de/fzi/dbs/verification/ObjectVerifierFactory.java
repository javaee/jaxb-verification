package de.fzi.dbs.verification;

import com.sun.xml.bind.Messages;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for the object verifier factories.
 *
 * @author Aleksei Valikov
 */
public abstract class ObjectVerifierFactory
{
  /**
   * Class/object verifier class mapping.
   */
  protected static Map objectVerifierClasses = new HashMap();

  /**
   * Returns an instance of object verifier for the given class. The class may be either
   * content interface or implementing class, so <code>
   *
   * @param theClass class of the object to be verified.
   * @return Object verifier for the given object class.
   * @throws JAXBException In case object verifier could not be found or instantiated.
   */
  public ObjectVerifier newInstance(final Class theClass) throws JAXBException
  {
    try
    {
      final Class objectVerifierClass = (Class) objectVerifierClasses.get(theClass);
      if (null == objectVerifierClass)
      {
        throw new JAXBException(Messages.format(Messages.MISSING_INTERFACE, theClass));
      }
      return (ObjectVerifier) objectVerifierClass.newInstance();
    }
    catch (Exception ex)
    {
      throw new JAXBException(ex);
    }
  }

}
