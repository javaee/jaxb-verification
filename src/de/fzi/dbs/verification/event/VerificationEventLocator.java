package de.fzi.dbs.verification.event;

import com.sun.xml.bind.ValidationEventLocatorEx;
import org.w3c.dom.Node;

import java.net.URL;

/**
 * Verification event locator.
 *
 * @author Aleksei Valikov
 */
public class VerificationEventLocator implements ValidationEventLocatorEx
{
  /**
   * Parent locator.
   */
  protected final VerificationEventLocator parentLocator;

  /**
   * Object.
   */
  protected final Object object;

  /**
   * Field name.
   */
  protected final String fieldName;

  /**
   * Constructs a new verification event locator.
   *
   * @param parentLocator parent location (may be <code>null</code>).
   * @param object        object.
   * @param fieldName     field name.
   */
  public VerificationEventLocator(final VerificationEventLocator parentLocator, final Object object, final String fieldName)
  {
    this.object = object;
    this.fieldName = fieldName;
    this.parentLocator = parentLocator;
  }

  /**
   * Returns parent locator.
   * 
   * @return Parent locator.
   */
  public VerificationEventLocator getParentLocator()
  {
    return parentLocator;
  }

  public Object getObject()
  {
    return object;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public int getColumnNumber()
  {
    return 0;
  }

  public int getLineNumber()
  {
    return 0;
  }

  public int getOffset()
  {
    return 0;
  }

  public URL getURL()
  {
    return null;
  }

  public Node getNode()
  {
    return null;
  }

  public String toString()
  {
    final StringBuffer sb = new StringBuffer();
    sb.append("Object [");
    sb.append(getObject());
    sb.append("], field [");
    sb.append(getFieldName());
    sb.append("].");
    return sb.toString();
  }

  /**
   * Returns step for this locator. The step is a single part of location expression.
   *
   * @return Step for this locator.
   */
  public String getStep()
  {
    return getFieldName();
  }

  /**
   * Returns EL-style expression identifying location.
   *
   * @return EL-style expression identifying location.
   */
  public String getELExpression()
  {
    return ((null == getParentLocator()) ? "" : getParentLocator().getELExpression() + ".") + getStep();
  }

  /**
   * Returns XPath-expression identifying location.
   *
   * @return XPath-expression identifying location.
   */
  public String getXPath()
  {
    return ((null == getParentLocator()) ? "" : getParentLocator().getXPath() + "/") + getStep();
  }
}
