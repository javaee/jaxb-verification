package de.fzi.dbs.verification.structure.problem;

/**
 * Too many elements problem.
 */
public class TooManyElementsProblem extends ElementsNumberProblem
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
