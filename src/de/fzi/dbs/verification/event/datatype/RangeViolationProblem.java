package de.fzi.dbs.verification.event.datatype;

/**
 * Value violates range.
 *
 * @author Aleksei Valikov
 */
public class RangeViolationProblem extends ValueProblem
{
  /**
   * Limiting value.
   */
  protected Object limitValue;

  /**
   * Constructs a new problem description.
   *
   * @param value      value.
   * @param limitValue limiting value.
   */
  public RangeViolationProblem(final Object value, final Object limitValue)
  {
    super(value);
    this.limitValue = limitValue;
  }

  /**
   * Returns limiting value.
   *
   * @return Limiting value.
   */
  public Object getLimitValue()
  {
    return limitValue;
  }

  public Object[] getMessageParameters()
  {
    return new Object[]{getValue(), getLimitValue()};
  }

}
