package de.fzi.dbs.verification.event;

import de.fzi.dbs.verification.problem.Problem;

/**
 * Structural event describes problems with object structure.
 * @author Aleksei Valikov
 */
public class StructuralEvent extends VerificationEvent
{
  /**
   * Constructs a new structural event.
   * @param locator locator (where).
   * @param problem problem (what).
   */
  public StructuralEvent(final VerificationEventLocator locator, final Problem problem)
  {
    super(locator, problem);
  }

  public String getMessage()
  {
    // todo
    return "todo";
  }
}
