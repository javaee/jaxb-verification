package de.fzi.dbs.verification.event.datatype;

/**
 * Value is too short problem.
 *
 * @author Aleksei Valikov
 */
public class TooShortProblem extends LengthProblem
{
  /**
   * Constructs a new "value is too short" problem description.
   *
   * @param value       value.
   * @param valueLength value length.
   * @param length      min length.
   */
  public TooShortProblem(final Object value, final int valueLength, final int length)
  {
    super(value, valueLength, length);
  }
}
