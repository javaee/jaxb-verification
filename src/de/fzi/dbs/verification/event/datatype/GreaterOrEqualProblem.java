package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.datatype.*;

/**
 * Value >= limiting value.
 *
 * @author Aleksei Valikov
 */
public class GreaterOrEqualProblem extends de.fzi.dbs.verification.event.datatype.RangeViolationProblem
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
