package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.datatype.*;

/**
 * Invalid name.
 *
 * @author Aleksei Valikov
 */
public class NameProblem extends de.fzi.dbs.verification.event.datatype.StringFormatProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value value.
   */
  public NameProblem(final Object value)
  {
    super(value);
  }
}
