package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.Problem;

import java.util.ResourceBundle;

/**
 * Descrribes a problem with a certain value.
 */
public abstract class ValueProblem
  extends Problem
{
  /**
   * Value that causes the problem.
   */
  protected Object value;

  /**
   * Constructs a new problem.
   *
   * @param value value that causes the problem.
   */
  public ValueProblem(final Object value)
  {
    this.value = value;
  }

  /**
   * Returns value that causes the problem.
   *
   * @return Value that causes the problem.
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * Returns an array of parameters used to format a message. First object of the array
   * (<code>getMessageParameters()[0]</code>) must always be the value that causes the problem.
   * Subclasses should document their message parameters.
   * This implementation returns an array of single <code>getValue()</code> object.
   *
   * @return Array of message parameters used to format a message.
   */
  public Object[] getMessageParameters()
  {
    return new Object[]{getValue()};
  }

  public ResourceBundle getDefaultResourceBundle()
  {
    return ResourceBundle.getBundle(getClass().getPackage().getName() + ".Messages");
  }
}
