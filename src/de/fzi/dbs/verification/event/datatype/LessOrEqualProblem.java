package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.datatype.*;

/**
 * Value <= limiting value.
 *
 * @author Aleksei Valikov
 */
public class LessOrEqualProblem extends de.fzi.dbs.verification.event.datatype.RangeViolationProblem
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
