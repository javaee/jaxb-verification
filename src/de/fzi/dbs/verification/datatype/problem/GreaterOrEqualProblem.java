package de.fzi.dbs.verification.datatype.problem;

/**
 * Value >= limiting value.
 *
 * @author Aleksei Valikov
 */
public class GreaterOrEqualProblem extends RangeViolationProblem
{
  /**
   * Constructs a new problem.
   *
   * @param value      effective value.
   * @param limitValue limiting value.
   */
  public GreaterOrEqualProblem(final Object value, final Object limitValue)
  {
    super(value, limitValue);
  }
}
