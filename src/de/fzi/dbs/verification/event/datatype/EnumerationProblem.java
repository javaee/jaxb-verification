package de.fzi.dbs.verification.event.datatype;

import java.util.Set;

/**
 * The value does not fulfill the enumeration facet.
 * @author Aleksei Valikov
 */
public class EnumerationProblem extends ValueProblem
{
  /**
   * Set of expected enumeration values.
   */
  protected Set values;

  /**
   * Constructs a new enumeration problem.
   * @param value the offending value.
   * @param values the set of expected values of the enumeration.
   */
  public EnumerationProblem(final Object value, final Set values)
  {
    super(value);
    this.values = values;
  }

  /**
   * Returns the set of expected enumeration values.
   * @return Expected enumeration values.
   */
  public Set getValues()
  {
    return values;
  }

  public Object[] getMessageParameters()
  {
    return new Object[]{getValue(), getValues()};
  }

  public boolean equals(final Object obj)
  {
    if (obj instanceof EnumerationProblem)
    {
      final EnumerationProblem enumerationProblem = (EnumerationProblem) obj;
      return getValue().equals(enumerationProblem.getValue()) &&
        getValues().equals(enumerationProblem.getValues());
    }
    else
    {
      return false;
    }
  }
}
