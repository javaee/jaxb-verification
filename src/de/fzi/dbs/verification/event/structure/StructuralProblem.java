package de.fzi.dbs.verification.event.structure;

import de.fzi.dbs.verification.event.Problem;

import java.util.ResourceBundle;

/**
 * Base class for structural problems.
 *
 * @author Aleksei Valikov
 */
public abstract class StructuralProblem extends Problem
{
  public ResourceBundle getDefaultResourceBundle()
  {
    return ResourceBundle.getBundle(getClass().getPackage().getName() + ".Messages");
  }
}
