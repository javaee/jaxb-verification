package de.fzi.dbs.verification.datatype.problem;

import org.apache.commons.lang.Validate;

/**
 * String format problem.
 *
 * @author Aleksei Valikov
 */
public abstract class StringFormatProblem extends ValueProblem
{
  /**
   * Constructs a new string format problem. Value must be a string.
   *
   * @param value value.
   */
  protected StringFormatProblem(final Object value)
  {
    super(value);
    Validate.isTrue(value instanceof String, "Value must be string.");
  }

  /**
   * Returns value as string.
   * 
   * @return Value as string.
   */
  public String getValueAsString()
  {
    return (String) value;
  }
}
