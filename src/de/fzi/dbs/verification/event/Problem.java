package de.fzi.dbs.verification.event;

import de.fzi.dbs.verification.Reportable;
import org.apache.commons.lang.ArrayUtils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Root class for problems.
 *
 * @author Aleksei Valikov
 */
public abstract class Problem extends Exception implements Reportable
{
  /**
   * Returns code of the problem. By default, code of the problem is problem's class name.
   *
   * @return String code that uniquely identifies this problem. May be used to reference messages.
   */
  public String getMessageCode()
  {
    return getClass().getName();
  }

  /**
   * Returns parameters used to format the problem message.
   *
   * @return Array of parameters used to format problem message.
   */
  public abstract Object[] getMessageParameters();

  /**
   * Formats problem message using the given resource bundle.
   *
   * @param bundle bundle to use resources from.
   * @return Formatted message.
   */
  public String getMessage(final ResourceBundle bundle)
  {
    try
    {
      final String messageTemplate = bundle.getString(getMessageCode());
      final String message = MessageFormat.format(messageTemplate, getMessageParameters());
      return message;
    }
    catch (MissingResourceException mrex)
    {
      return "Message [" + getMessageCode() + "].";
    }
  }

  /**
   * Returns problem message formatted using the {@link #getDefaultResourceBundle() default resource bundle}.
   *
   * @return Formatted message.
   */
  public String getMessage()
  {
    return getMessage(getDefaultResourceBundle());
  }

  /**
   * Returns the default resource bundle.
   *
   * @return Default resource bundle.
   */
  public abstract ResourceBundle getDefaultResourceBundle();

  public boolean equals(Object obj)
  {
    boolean result = false;
    if (obj instanceof Problem)
    {
      final Problem problem = (Problem) obj;
      result = ArrayUtils.isEquals(getMessageParameters(), problem.getMessageParameters());
    }
    return result;
  }

  public int hashCode()
  {
    int hashCode = 0;
    final Object[] messageParameters = getMessageParameters();
    for (int index = 0; index < messageParameters.length; index++)
    {
      hashCode = hashCode * 31 + messageParameters[index].hashCode();
    }
    return hashCode;
  }

  public String toString()
  {
    return getMessage();
  }
}
