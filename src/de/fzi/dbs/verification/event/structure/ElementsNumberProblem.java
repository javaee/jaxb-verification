package de.fzi.dbs.verification.event.structure;

import de.fzi.dbs.verification.event.Problem;

/**
 * Base class for problems describing wrong number of elements in the collection.
 * @author Aleksei Valikov
 */
public abstract class ElementsNumberProblem extends StructuralProblem
{
  /** Effective count. */
  protected int count;

  /** Expected count. */
  protected int expected;

  /**
   * Constructs a new elements number problem.
   * @param count effective count.
   * @param expected expected count.
   */
  protected ElementsNumberProblem(final int count, final int expected)
  {
    this.count = count;
    this.expected = expected;
  }

  /**
   * Returns effective count.
   * @return Effective count.
   */
  public int getCount()
  {
    return count;
  }

  /**
   * Returns expected count.
   * @return Expected count.
   */
  public int getExpected()
  {
    return expected;
  }

  public Object[] getMessageParameters()
  {
    return new Object[]{new Integer(getCount()), new Integer(getExpected())};
  }
}
