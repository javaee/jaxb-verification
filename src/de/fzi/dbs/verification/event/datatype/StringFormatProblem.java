package de.fzi.dbs.verification.event.datatype;

import org.apache.commons.lang.Validate;
import de.fzi.dbs.verification.event.datatype.*;

/**
 * String format problem.
 *
 * @author Aleksei Valikov
 */
public abstract class StringFormatProblem extends de.fzi.dbs.verification.event.datatype.ValueProblem
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
