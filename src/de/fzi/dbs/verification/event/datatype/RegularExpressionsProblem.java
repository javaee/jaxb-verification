package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.Problem;

/**
 * Value does not match regular expressions.
 * @author Aleksei Valikov
 */
public class RegularExpressionsProblem extends ValueProblem
{
  /**
   * Array of patterns that value failed to match.
   */
  protected final String[] patterns;

  /**
   * Constructs a new regular expressions problem.
   * @param value the value.
   * @param patterns array of patterns.
   */
  public RegularExpressionsProblem(final Object value, final String[] patterns)
  {
    super(value);
    this.patterns = patterns;
  }

  /**
   * Returns array of patterns that expression failed to match.
   * @return Array of patterns that expression failed to match.
   */
  public String[] getPatterns()
  {
    return patterns;
  }

  public Object[] getMessageParameters()
  {
    return new Object[]{getValue(), getPatterns()};
  }
}
