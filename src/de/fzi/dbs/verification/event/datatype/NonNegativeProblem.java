package de.fzi.dbs.verification.event.datatype;

/**
 * Non-negative value.
 *
 * @author Aleksei Valikov
 */
public class NonNegativeProblem extends SignProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value value.
   */
  public NonNegativeProblem(final Object value)
  {
    super(value);
  }
}
