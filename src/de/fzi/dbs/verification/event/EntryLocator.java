package de.fzi.dbs.verification.event;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Locator for the collection entry.
 */
public class EntryLocator extends VerificationEventLocator
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
  public EntryLocator(final VerificationEventLocator parentLocator, final Object object, final String fieldName, final int index)
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

  public String toString()
  {
    final StringBuffer sb = new StringBuffer();
    sb.append("Object [");
    sb.append(getObject());
    sb.append("], field [");
    sb.append(getFieldName());
    sb.append("], entry index [");
    sb.append(getIndex());
    sb.append("].");
    return sb.toString();
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
}
