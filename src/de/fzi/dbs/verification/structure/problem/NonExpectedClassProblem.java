package de.fzi.dbs.verification.structure.problem;

import de.fzi.dbs.verification.problem.Problem;

/**
 * Not expected class problem.
 *
 * @author Aleksei Valikov
 */
public class NonExpectedClassProblem extends Problem
{
  /**
   * Effective class.
   */
  protected Class theClass;

  /**
   * Constructs a new problem.
   *
   * @param theClass effective class.
   */
  public NonExpectedClassProblem(final Class theClass)
  {
    this.theClass = theClass;
  }

  /**
   * Returns effective class.
   *
   * @return Effective class.
   */
  public Class getEffectiveClass()
  {
    return theClass;
  }

  public Object[] getMessageParameters()
  {
    return new Object[]{getEffectiveClass()};
  }
}
