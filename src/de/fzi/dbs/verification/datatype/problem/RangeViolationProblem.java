package de.fzi.dbs.verification.datatype.problem;

/**
 * Value violates range.
 *
 * @author Aleksei Valikov
 */
public class RangeViolationProblem extends Problem
{
  /**
   * Limiting value.
   */
  protected Object limitValue;

  /**
   * Constructs a new problem description.
   *
   * @param value      value.
   * @param limitValue limiting value.
   */
  public RangeViolationProblem(final Object value, final Object limitValue)
  {
    super(value);
    this.limitValue = limitValue;
  }
}
