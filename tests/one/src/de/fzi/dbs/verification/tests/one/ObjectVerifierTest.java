package de.fzi.dbs.verification.tests.one;

import de.fzi.dbs.verification.addon.tests.AbstractVerifierTest;
import de.fzi.dbs.verification.event.EntryLocator;
import de.fzi.dbs.verification.event.VerificationEvent;
import de.fzi.dbs.verification.event.VerificationEventLocator;
import de.fzi.dbs.verification.event.datatype.EnumerationProblem;
import de.fzi.dbs.verification.event.datatype.RegularExpressionProblem;
import de.fzi.dbs.verification.tests.one.impl.ElementWithListAttribute1Impl;
import de.fzi.dbs.verification.tests.one.impl.FacetTypeImpl;
import de.fzi.dbs.verification.tests.one.impl.RootImpl;

import javax.xml.bind.ValidationEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;

/**
 * @author Aleksei Valikov
 */
public class ObjectVerifierTest extends AbstractVerifierTest
{
  /**
   * Constructs the test.
   *
   * @param name test name.
   */
  public ObjectVerifierTest(final String name)
  {
    super(name);
  }

  protected de.fzi.dbs.verification.ObjectVerifierFactory getObjectVerifierFactory()
  {
    return new ObjectVerifierFactory();
  }

  protected Object[] getObjects()
  {
    return new Object[]{root};
  }

  protected final Root root;

  protected final ElementWithListAttribute1 elementWithListAttribute1;

  protected final FacetType facetType;

  {
    root = new RootImpl();
    elementWithListAttribute1 = new ElementWithListAttribute1Impl();
    root.setElementWithListAttribute1(elementWithListAttribute1);
    elementWithListAttribute1.getList1().add("abcd");

    facetType = new FacetTypeImpl();
    root.setFacet(facetType);
    root.getFacet().setStringEnumeration("e");
  }

  protected ValidationEvent[][] getEvents()
  {
    return new ValidationEvent[][]{{
      new VerificationEvent(new EntryLocator(new VerificationEventLocator(null,
        root,
        "ElementWithListAttribute1"),
        elementWithListAttribute1,
        "List1",
        0),
        new RegularExpressionProblem("abcd", "[A-Z]{2}([0-9]|[A-Z]){7}")),

      new VerificationEvent(new VerificationEventLocator(new VerificationEventLocator(null,
        root,
        "Facet"),
        facetType,
        "StringEnumeration"),
        new EnumerationProblem("e", new HashSet(Arrays.asList(new Object[]{"a", "b", "c", "d"})))),

      new VerificationEvent(new VerificationEventLocator(new VerificationEventLocator(null,
        root,
        "Facet"),
        facetType,
        "IntEnumeration"),
        new EnumerationProblem(new Integer(0), new HashSet(Arrays.asList(new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4)}))))
    }};
  }
}
