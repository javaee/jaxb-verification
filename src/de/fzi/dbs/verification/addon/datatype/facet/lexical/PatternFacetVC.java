package de.fzi.dbs.verification.addon.datatype.facet.lexical;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import com.sun.msv.datatype.xsd.PatternFacet;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import de.fzi.dbs.verification.Util;
import de.fzi.dbs.verification.event.datatype.RegularExpressionProblem;
import de.fzi.dbs.verification.event.datatype.RegularExpressionsProblem;

/**
 * VC for pattern facets.
 *
 * @author Aleksei Valikov
 */
public class PatternFacetVC extends DataTypeWithLexicalConstraintFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final PatternFacet patternFacet = (PatternFacet) datatype;
    final JType regExpType = codeModel.ref(RegularExpression.class);
    if (patternFacet.patterns.length == 1)
    {
      String pattern = patternFacet.patterns[0];
      block._if(JOp.not(JExpr._new(regExpType).arg(JExpr.lit(pattern)).arg(JExpr.lit("X")).invoke("matches").arg(value.invoke("toString"))))
        ._then().assign(problem, JExpr._new(codeModel.ref(RegularExpressionProblem.class)).arg(value).arg(JExpr.lit(pattern)));
    }
    else if (patternFacet.patterns.length > 1)
    {
      final String patternsFieldName = Util.generateFieldName(theClass, "patterns");
      final String regExpsFieldName = Util.generateFieldName(theClass, "regExps");
      final JArray regExpsArray = JExpr.newArray(regExpType);
      final JArray patternsArray = JExpr.newArray(codeModel.ref(String.class));

      for (int index = 0; index < patternFacet.patterns.length; index++)
      {
        regExpsArray.add(JExpr._new(regExpType).arg(JExpr.lit(patternFacet.patterns[index])).arg(JExpr.lit("X")));
        patternsArray.add(JExpr.lit(patternFacet.patterns[index]));
      }
      final JFieldVar regExps = theClass.field(JMod.PROTECTED, regExpType.array(), regExpsFieldName, regExpsArray);
      final JFieldVar patterns = theClass.field(JMod.PROTECTED, codeModel.ref(String.class).array(), patternsFieldName, patternsArray);

      final JVar matches = block.decl(codeModel.INT, "matches", JExpr.lit(0));
      final JForLoop _for = block._for();
      final JVar index = _for.init(codeModel.INT, "index", JExpr.lit(0));
      _for.test(JOp.lt(index, regExps.ref("length")));
      _for.update(JOp.incr(index));
      _for.body()._if(regExps.component(index).invoke("matches").arg(value.invoke("toString")))._then().assignPlus(matches, JExpr.lit(1));
      final JConditional ifNoMatches = block._if(JOp.eq(matches, JExpr.lit(0)));
      ifNoMatches._then().assign(problem, JExpr._new(codeModel.ref(RegularExpressionsProblem.class)).arg(value).arg(patterns));
    }
    return block;
  }
}
