package de.fzi.dbs.verification.handler;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import java.util.Collection;
import java.util.Iterator;

/**
 * Dispatching validation event handler routes events to the interested listeners.
 *
 * @author Aleksei Valikov
 */
public class DispatchingValidationEventHandler implements ValidationEventHandler, ValidationEventDispatcher
{
  /**
   * Map of registered handlers.
   */
  protected MultiMap handlersMap = new MultiHashMap();

  public void registerValidationEventHandler(final ValidationEventLocator locator, final ValidationEventHandler handler)
  {
    handlersMap.put(locator, handler);
  }

  public void unregisterValidationEventHandler(final ValidationEventLocator locator, final ValidationEventHandler handler)
  {
    handlersMap.remove(locator, handler);
  }

  public boolean handleEvent(final ValidationEvent event)
  {
    final Collection listeners = (Collection) handlersMap.get(event.getLocator());
    if (null != listeners)
    {
      for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
      {
        final ValidationEventHandler handler = (ValidationEventHandler) iterator.next();
        handler.handleEvent(event);
      }
    }
    return true;
  }

}
