package de.fzi.dbs.verification.handler;

import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.ValidationEventHandler;

/**
 * Validation event dispatcher interface.
 * @author Aleksei Valikov
 */
public interface ValidationEventDispatcher
{
  /**
   * Registers a handler that will receive validation messages related to the certain location.
   * @param locator locator.
   * @param handler listening handler.
   */
  public void registerValidationEventHandler(final ValidationEventLocator locator, ValidationEventHandler handler);

  /**
   * Unegisters a listening handler.
   * @param locator locator.
   * @param handler listening handler to be unregistered.
   */
  public void unregisterValidationEventHandler(final ValidationEventLocator locator, ValidationEventHandler handler);
}
