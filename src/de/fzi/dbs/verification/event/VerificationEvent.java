package de.fzi.dbs.verification.event;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * @author Aleksei Valikov
 */
public abstract class VerificationEvent implements ValidationEvent
{
  protected final VerificationEventLocator locator;
  protected final Throwable problem;

  protected VerificationEvent(final VerificationEventLocator locator, final Throwable problem)
  {
    this.locator = locator;
    this.problem = problem;
  }

  /**
   * Verification events are always {@link ValidationEvent#WARNING}.
   *
   * @return Event severity.
   */
  public int getSeverity()
  {
    return ValidationEvent.WARNING;
  }

  public ValidationEventLocator getLocator()
  {
    return locator;
  }

  public VerificationEventLocator getVerificationEventLocator()
  {
    return locator;
  }

  public Throwable getLinkedException()
  {
    return problem;
  }


}
