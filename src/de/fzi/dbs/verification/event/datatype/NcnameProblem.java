package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.datatype.*;

/**
 * Invalid ncname.
 *
 * @author Aleksei Valikov
 */
public class NcnameProblem extends de.fzi.dbs.verification.event.datatype.StringFormatProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value value.
   */
  public NcnameProblem(final Object value)
  {
    super(value);
  }
}
