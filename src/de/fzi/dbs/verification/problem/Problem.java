package de.fzi.dbs.verification.problem;

/**
 * Root class for problems.
 *
 * @author Aleksei Valikov
 */
public abstract class Problem extends Exception
{
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
   * Returns parameters used to format the problem message.
   * 
   * @return Array of parameters used to format problem message.
   */
  public abstract Object[] getMessageParameters();

}
