package de.fzi.dbs.verification.datatype.problem;

/**
 * Descrribes a problem with a certain value.
 */
public abstract class Problem extends Exception
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
  public Problem(final Object value)
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
   * Returns code of the problem. By default, code of the problem is problem's class name.
   *
   * @return String code that uniquely identifies this problem. May be used to reference messages.
   */
  public String getCode()
  {
    return getClass().getName();
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
}
