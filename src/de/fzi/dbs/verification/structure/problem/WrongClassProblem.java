package de.fzi.dbs.verification.structure.problem;

/**
 * Wrong class problem.
 */
public class WrongClassProblem extends NonExpectedClassProblem
{
  /**
   * Expected class.
   */
  protected Class expectedClass;

  /**
   * Constructs a new problem.
   *
   * @param theClass      effective class.
   * @param expectedClass expected class.
   */
  public WrongClassProblem(final Class theClass, final Class expectedClass)
  {
    super(theClass);
    this.expectedClass = expectedClass;
  }

  /**
   * Returns expected class.
   * 
   * @return Expected class.
   */
  public Class getExpectedClass()
  {
    return expectedClass;
  }
}
