package de.fzi.dbs.verification.event.structure;

import de.fzi.dbs.verification.event.structure.*;

/**
 * Wrong class problem.
 */
public class WrongClassProblem extends de.fzi.dbs.verification.event.structure.NonExpectedClassProblem
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
