package de.fzi.dbs.verification.event;

/**
 * @author Aleksei Valikov
 */
public class DatatypeEvent extends VerificationEvent
{
  public DatatypeEvent(final VerificationEventLocator locator, final Throwable problem)
  {
    super(locator, problem);
  }

  public String getMessage()
  {
    return "todo";
  }
}
