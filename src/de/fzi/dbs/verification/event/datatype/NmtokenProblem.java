package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.datatype.*;

/**
 * Invalid nmtoken.
 *
 * @author Aleksei Valikov
 */
public class NmtokenProblem extends de.fzi.dbs.verification.event.datatype.ValueProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value value.
   */
  public NmtokenProblem(final Object value)
  {
    super(value);
  }
}
