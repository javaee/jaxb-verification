package de.fzi.dbs.verification.event;

public class EntryLocator extends VerificationEventLocator
{
  protected final int index;

  public EntryLocator(final VerificationEventLocator parentLocator, final Object object, final String fieldName, final int index)
  {
    super(parentLocator, object, fieldName);
    this.index = index;
  }
}
