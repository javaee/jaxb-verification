package de.fzi.dbs.verification.event;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Locator for the collection entry.
 */
public final class EntryLocator extends AbstractVerificationEventLocator
{
  /**
   * Entry index.
   */
  protected final int index;

  /**
   * Constructs a new entry locator.
   *
   * @param parentLocator parent locator.
   * @param object        master object.
   * @param fieldName     field name.
   * @param index         entry index.
   */
  public EntryLocator(final AbstractVerificationEventLocator parentLocator, final Object object, final String fieldName, final int index)
  {
    super(parentLocator, object, fieldName);
    this.index = index;
  }

  /**
   * Constructs a new entry locator.
   *
   * @param locator collection locator.
   * @param index   entry index.
   */
  public EntryLocator(final VerificationEventLocator locator, final int index)
  {
    super(locator.getParentLocator(), locator.getObject(), locator.getFieldName());
    this.index = index;
  }

  /**
   * Returns entry index.
   *
   * @return Index of the entry.
   */
  public int getIndex()
  {
    return index;
  }

  /**
   * Returns step expression (<code><em>name</em>[<em>index</em>]</code>).
   *
   * @return Step expression.
   */
  public String getStep()
  {
    return getFieldName() + "[" + getIndex() + "]";
  }


  public Object[] getMessageParameters()
  {
    return new Object[]
    {
      getObject(),
      getFieldName(),
      new Integer(getIndex()),
      getELExpression(),
      getJXPathExpression(),
    };
  }

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
        MessageFormat.format("Object: {0}\nField: {1}\nEntry index: {2}.\nEL: {3}\nJXPath: {4}.", getMessageParameters());
    }
  }

  public boolean equals(final Object obj)
  {
    boolean result = false;
    if (obj instanceof EntryLocator)
    {
      final EntryLocator locator = (EntryLocator) obj;
      result = (getObject() == locator.getObject()) &&
        (getFieldName().equals(locator.getFieldName())) &&
        getIndex() == locator.getIndex();

    }
    return result;
  }

  public int hashCode()
  {
    return super.hashCode() * 23 + getIndex();
  }
}
