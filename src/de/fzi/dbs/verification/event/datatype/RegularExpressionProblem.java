package de.fzi.dbs.verification.event.datatype;

/**
 * Values does not match a single regular expression.
 * @author Aleksei Valikov
 */
public class RegularExpressionProblem extends RegularExpressionsProblem
{
  /**
   * Constructs a new problem.
   * @param value the value.
   * @param pattern regular expression pattern.
   */
  public RegularExpressionProblem(final Object value, final String pattern)
  {
    super(value, new String[]{pattern});
  }

  /**
   * Returns pattern that value failed to match.
   * @return The pattern.
   */
  public String getPattern()
  {
    return getPatterns()[0];
  }

  public Object[] getMessageParameters()
  {
    return new Object[]{getValue(), getPattern()};
  }
}
