package de.fzi.dbs.verification.event.datatype;

/**
 * Invalid language.
 *
 * @author Aleksei Valikov
 */
public class LanguageProblem extends StringFormatProblem
{
  /**
   * Constructs a new problem.
   *
   * @param value value.
   */
  public LanguageProblem(final Object value)
  {
    super(value);
  }
}
