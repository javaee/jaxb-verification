package de.fzi.dbs.verification.event.structure;

import de.fzi.dbs.verification.event.structure.*;

/**
 * Too many elements problem.
 */
public class TooManyElementsProblem extends de.fzi.dbs.verification.event.structure.ElementsNumberProblem
{
  /**
   * Constructs a new problem.
   * @param count count.
   * @param expected maximum count.
   */
  public TooManyElementsProblem(final int count, final int expected)
  {
    super(count, expected);
  }
}
