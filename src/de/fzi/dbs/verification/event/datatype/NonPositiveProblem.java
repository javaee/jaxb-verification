package de.fzi.dbs.verification.event.datatype;

/**
 * Non-positive value.
 *
 * @author Aleksei Valikov
 */
public class NonPositiveProblem extends SignProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value value.
   */
  public NonPositiveProblem(final Object value)
  {
    super(value);
  }
}
