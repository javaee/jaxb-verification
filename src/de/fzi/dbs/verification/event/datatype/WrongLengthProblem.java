package de.fzi.dbs.verification.event.datatype;

import de.fzi.dbs.verification.event.datatype.*;

/**
 * Value has a wrong length.
 * 
 * @author Aleksei Valikov
 */
public class WrongLengthProblem extends de.fzi.dbs.verification.event.datatype.LengthProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value       value.
   * @param valueLength effective length.
   * @param length      expected length.
   */
  public WrongLengthProblem(final Object value, final int valueLength, final int length)
  {
    super(value, valueLength, length);
  }
}
