package de.fzi.dbs.verification.event;

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
}
