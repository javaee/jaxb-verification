package de.fzi.dbs.verification.datatype.problem;

/**
 * Value <= limiting value.
 *
 * @author Aleksei Valikov
 */
public class LessOrEqualProblem extends RangeViolationProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value      value.
   * @param limitValue limiting value.
   */
  public LessOrEqualProblem(final Object value, final Object limitValue)
  {
    super(value, limitValue);
  }
}
