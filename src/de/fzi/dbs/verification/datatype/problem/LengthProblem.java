package de.fzi.dbs.verification.datatype.problem;

/**
 * Describes a length problem - too short, too long or wrong length.
 *
 * @author Aleksei Valikov
 */
public abstract class LengthProblem extends ValueProblem
{
  /**
   * Effective length of the value.
   */
  protected int valueLength;

  /**
   * Expected length parameter.
   */
  protected int length;

  /**
   * Constructs a new length problem description.
   *
   * @param value       value.
   * @param valueLength length of the value.
   * @param length      expected length.
   */
  public LengthProblem(final Object value, final int valueLength, final int length)
  {
    super(value);
    this.valueLength = valueLength;
    this.length = length;
  }

  /**
   * Returns length of the value.
   *
   * @return Length of the value.
   */
  public int getValueLength()
  {
    return valueLength;
  }

  /**
   * Returns expected length.
   *
   * @return Expected length.
   */
  public int getLength()
  {
    return length;
  }

  /**
   * Returns message parameters: value, value length and length.
   *
   * @return Message parameters.
   */
  public Object[] getMessageParameters()
  {
    return new Object[]{getValue(), new Integer(getValueLength()), new Integer(getLength())};
  }
}
