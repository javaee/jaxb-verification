package de.fzi.dbs.verification.event.structure;

import de.fzi.dbs.verification.event.Problem;
import de.fzi.dbs.verification.event.structure.*;

/**
 * Trivial problem which has no parameters.
 *
 * @author Aleksei Valikov
 */
public abstract class TrivialProblem extends de.fzi.dbs.verification.event.structure.StructuralProblem
{
  public Object[] getMessageParameters()
  {
    return new Object[0];
  }
}
