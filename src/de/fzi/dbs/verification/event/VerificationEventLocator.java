package de.fzi.dbs.verification.event;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Verification event locator.
 *
 * @author Aleksei Valikov
 */
public final class VerificationEventLocator extends AbstractVerificationEventLocator
{
  /**
   * Constructs a new verification event locator.
   *
   * @param parentLocator parent location (may be <code>null</code>).
   * @param object        object.
   * @param fieldName     field name.
   */
  public VerificationEventLocator(final AbstractVerificationEventLocator parentLocator, final Object object, final String fieldName)
  {
    super(parentLocator, object, fieldName);
  }

  /**
   * Returns step for this locator. The step is a single part of location expression.
   *
   * @return Step for this locator.
   */
  public String getStep()
  {
    return getFieldName();
  }

  /**
   * Returns parameters for message formatting.
   *
   * @return Message formatting parameters.
   */
  public Object[] getMessageParameters()
  {
    return new Object[]
    {
      getObject(),
      getFieldName(),
      getELExpression(),
      getJXPathExpression(),
    };
  }

  /**
   * Returns location message.
   *
   * @param bundle resource bundle to be used to locate message template.
   * @return location message.
   */
  public String getMessage(final ResourceBundle bundle)
  {
    try
    {
      final String messageTemplate = bundle.getString(getMessageCode());
      return MessageFormat.format(messageTemplate, getMessageParameters());
    }
    catch (MissingResourceException mrex)
    {
      return
        MessageFormat.format("Object: {0}\nField: {1}\nEL: {2}\nJXPath: {3}.", getMessageParameters());
    }
  }

  public boolean equals(final Object obj)
  {
    boolean result = false;
    if (obj instanceof VerificationEventLocator)
    {
      final VerificationEventLocator locator = (VerificationEventLocator) obj;
      result = (getObject() == locator.getObject()) &&
        (getFieldName().equals(locator.getFieldName()));
    }
    return result;
  }

}
