package de.fzi.dbs.verification.structure.problem;

/**
 * Too few elements problem.
 */
public class TooFewElementsProblem
  extends ElementsNumberProblem
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
