package de.fzi.dbs.verification.event;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Verification event.
 *
 * @author Aleksei Valikov
 */
public class VerificationEvent implements ValidationEvent
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
   *
   * @param locator locator (where).
   * @param problem problem (what).
   */
  public VerificationEvent(final VerificationEventLocator locator, final de.fzi.dbs.verification.event.Problem problem)
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
   *
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

  /**
   * Returns the linked problem.
   *
   * @return The problem.
   */
  public Problem getProblem()
  {
    return problem;
  }

  /**
   * Returns code of the message.
   *
   * @return Code of the message.
   */
  public String getMessageCode()
  {
    return VerificationEvent.class.getName();
  }

  /**
   * Returns event message.
   *
   * @param bundle resource bundle.
   * @return event message.
   */
  public String getMessage(final ResourceBundle bundle)
  {
    final Object[] messageParameters = new Object[]{getVerificationEventLocator().getMessage(bundle),
                                                    getProblem().getMessage(bundle)};
    try
    {
      return MessageFormat.format(bundle.getString(getMessageCode()), messageParameters);

    }
    catch (MissingResourceException mrex)
    {
      return MessageFormat.format("Location:\n{0}\nProblem:\n{1}", messageParameters);
    }
  }

  /**
   * Returns event message.
   *
   * @return Event message.
   */
  public String getMessage()
  {
    final ResourceBundle bundle = ResourceBundle.getBundle(getClass().getPackage().getName() + ".Messages");
    final Object[] messageParameters = new Object[]{getVerificationEventLocator().getMessage(),
                                                    getProblem().getMessage()};
    try
    {
      return MessageFormat.format(bundle.getString(getMessageCode()), messageParameters);

    }
    catch (MissingResourceException mrex)
    {
      return MessageFormat.format("Location:\n{0}\nProblem:\n{1}", messageParameters);
    }
  }

  public int hashCode()
  {
    return getLinkedException().hashCode() + getLocator().hashCode() * 37;
  }

  public boolean equals(Object obj)
  {
    boolean result = false;
    if (obj instanceof ValidationEvent)
    {
      final ValidationEvent event = (ValidationEvent) obj;
      result = (getSeverity() == event.getSeverity()) &&
        (getLocator().equals(event.getLocator())) &&
        (getLinkedException().equals(event.getLinkedException()));

    }
      return result;
  }

  public String toString()
  {
    return getMessage();
  }
}
