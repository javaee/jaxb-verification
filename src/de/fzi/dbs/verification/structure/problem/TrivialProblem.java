package de.fzi.dbs.verification.structure.problem;

import de.fzi.dbs.verification.problem.Problem;

/**
 * Trivial problem which has no parameters.
 *
 * @author Aleksei Valikov
 */
public abstract class TrivialProblem extends Problem
{
  public Object[] getMessageParameters()
  {
    return new Object[0];
  }
}
