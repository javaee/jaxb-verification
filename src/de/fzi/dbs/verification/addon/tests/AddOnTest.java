package de.fzi.dbs.verification.addon.tests;

import de.fzi.dbs.jaxb.addon.test.AbstractAddOnTest;


/**
 * Add-on test.
 *
 * @author Aleksei Valikov
 */
public class AddOnTest extends AbstractAddOnTest
{
  /**
   * Constructs a new test case.
   *
   * @param name test name.
   */
  public AddOnTest(final String name)
  {
    super(name);
  }

  /**
   * Test setup.
   *
   * @throws Exception In case of setup problems.
   */
  public void setUp() throws Exception
  {
    super.setUp();
  }

  public String[] getAddonOptions()
  {
    return new String[]{"-nv", "-extension", "-Xverification"};
  }
}