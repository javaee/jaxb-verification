package de.fzi.dbs.verification.event.datatype;

/**
 * Invalid anyURI.
 *
 * @author Aleksei Valikov
 */
public class AnyURIProblem extends StringFormatProblem
{
  /**
   * Constructs a new problem description.
   *
   * @param value value.
   */
  public AnyURIProblem(final Object value)
  {
    super(value);
  }
}
