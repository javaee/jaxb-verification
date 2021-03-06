package de.fzi.dbs.verification.event.structure;

import de.fzi.dbs.verification.event.structure.*;

/**
 * Too few elements problem.
 */
public class TooFewElementsProblem
  extends de.fzi.dbs.verification.event.structure.ElementsNumberProblem
{
  /**
   * Constructs a new problem.
   *
   * @param count    effective count.
   * @param expected minimum count.
   */
  public TooFewElementsProblem(final int count, final int expected)
  {
    super(count, expected);
  }
}
