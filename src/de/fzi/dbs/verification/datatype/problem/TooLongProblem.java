package de.fzi.dbs.verification.datatype.problem;

/**
 * Value too long.
 *
 * @author Aleksei Valikov
 */
public class TooLongProblem extends LengthProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value       value.
   * @param valueLength effective length.
   * @param length      max length.
   */
  public TooLongProblem(final Object value, final int valueLength, final int length)
  {
    super(value, valueLength, length);
  }
}
