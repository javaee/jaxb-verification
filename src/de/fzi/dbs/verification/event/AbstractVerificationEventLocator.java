package de.fzi.dbs.verification.event;

import com.sun.xml.bind.ValidationEventLocatorEx;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.Node;
import de.fzi.dbs.verification.Reportable;

/**
 * @author Aleksei Valikov
 */
public abstract class AbstractVerificationEventLocator implements ValidationEventLocatorEx, Reportable
{
  /**
   * Parent locator.
   */
  protected final AbstractVerificationEventLocator parentLocator;
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
  protected AbstractVerificationEventLocator(final AbstractVerificationEventLocator parentLocator, final Object object, final String fieldName)
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
  public AbstractVerificationEventLocator getParentLocator()
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

  public abstract String getStep();

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
  public String getJXPathExpression()
  {
    return ((null == getParentLocator()) ? "" : getParentLocator().getJXPathExpression() + "/") + getStep();
  }

  public String toString()
  {
    return getMessage();
  }

  /**
   * Returns message code.
   *
   * @return Message code.
   */
  public String getMessageCode()
  {
    return getClass().getName();
  }

  public abstract Object[] getMessageParameters();

  public abstract String getMessage(ResourceBundle bundle);

  /**
   * Returns location message.
   *
   * @return Location message.
   */
  public String getMessage()
  {
    return getMessage(ResourceBundle.getBundle(getClass().getPackage().getName() + ".Messages"));
  }

  public int hashCode()
  {
    int hashCode = getObject().hashCode();
    hashCode = hashCode * 49 + getFieldName().hashCode();
    return hashCode;
  }
}
