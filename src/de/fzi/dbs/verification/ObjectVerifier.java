package de.fzi.dbs.verification;

import de.fzi.dbs.verification.event.VerificationEventLocator;
import de.fzi.dbs.verification.event.AbstractVerificationEventLocator;

import javax.xml.bind.ValidationEventHandler;

/**
 * Object verifier interface. Client application may use check methods to verify object structures.
 * {@link #check(javax.xml.bind.ValidationEventHandler, java.lang.Object) Three-argument method} is used
 * to check objects in a certain context,
 * {@link #check(javax.xml.bind.ValidationEventHandler, java.lang.Object) two-argument} method is used for
 * uncontexted checks.
 *
 * @author Aleksei Valikov
 */
public interface ObjectVerifier
{
  /**
   * Verifies object structure.
   *
   * @param handler handler used to report validation events.
   * @param object  object to check.
   */
  public void check(ValidationEventHandler handler, Object object);

  /**
   * Verifies object structure in a certain context.
   *
   * @param locator locator that defines the context.
   * @param handler handler used to report validation events.
   * @param object  object to check.
   */
  public void check(AbstractVerificationEventLocator locator, ValidationEventHandler handler, Object object);
}
