package de.fzi.dbs.verification.addon;

import com.sun.msv.grammar.ExpressionVisitor;
import com.sun.msv.grammar.OtherExp;
import com.sun.msv.grammar.xmlschema.OccurrenceExp;
import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.ExternalItem;
import com.sun.tools.xjc.grammar.FieldItem;
import com.sun.tools.xjc.grammar.IgnoreItem;
import com.sun.tools.xjc.grammar.InterfaceItem;
import com.sun.tools.xjc.grammar.JavaItem;
import com.sun.tools.xjc.grammar.JavaItemVisitor;
import com.sun.tools.xjc.grammar.PrimitiveItem;
import com.sun.tools.xjc.grammar.SuperClassItem;

/**
 * @author Aleksei Valikov
 */
public abstract class BGMVisitor implements JavaItemVisitor, ExpressionVisitor
{
  public Object onOther(final OtherExp exp)
  {
    if (exp instanceof JavaItem)
    {
      return ((JavaItem) exp).visitJI(this);
    }
    else
    {
      return exp.exp.visit(this);
    }
  }
}
