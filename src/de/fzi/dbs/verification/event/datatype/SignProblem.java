package de.fzi.dbs.verification.event.datatype;

import org.apache.commons.lang.Validate;
import de.fzi.dbs.verification.event.datatype.*;

/**
 * ValueProblem with a value sign - a numeric value has a wrong sign.
 * The value for this problem must be a {@link Number}.
 */
public class SignProblem extends de.fzi.dbs.verification.event.datatype.ValueProblem
{
  /**
   * Constructs a new sign problem.
   *
   * @param value problematic value.
   */
  public SignProblem(final Object value)
  {
    super(value);
    Validate.isTrue(value instanceof Number, "Value must be a number.");
  }

  /**
   * Returns value as {@link Number}.
   * 
   * @return Value as number.
   */
  public Number getValueAsNumber()
  {
    return (Number) value;
  }
}
