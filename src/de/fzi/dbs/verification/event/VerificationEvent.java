package de.fzi.dbs.verification.event;

import de.fzi.dbs.verification.problem.Problem;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * Verification event.
 *
 * @author Aleksei Valikov
 */
public abstract class VerificationEvent implements ValidationEvent
{
  /**
   * Event locator.
   */
  protected final VerificationEventLocator locator;

  /**
   * The problem.
   */
  protected final Problem problem;

  /**
   * Constructs a new verification event.
   * @param locator locator (where).
   * @param problem problem (what).
   */
  protected VerificationEvent(final VerificationEventLocator locator, final Problem problem)
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

  /**
   * Returns locator as a {@link VerificationEventLocator}.
   * @return Locator as a {@link VerificationEventLocator}. 
   */
  public VerificationEventLocator getVerificationEventLocator()
  {
    return locator;
  }

  public Throwable getLinkedException()
  {
    return problem;
  }


}
