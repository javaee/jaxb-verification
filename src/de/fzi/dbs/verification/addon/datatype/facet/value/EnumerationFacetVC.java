package de.fzi.dbs.verification.addon.datatype.facet.value;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.msv.datatype.xsd.DataTypeWithFacet;
import com.sun.msv.datatype.xsd.EnumerationFacet;
import de.fzi.dbs.verification.Util;
import de.fzi.dbs.verification.event.datatype.EnumerationProblem;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Enumeration facet.
 *
 * @author Aleksei Valikov
 */
public class EnumerationFacetVC extends DataTypeWithValueConstraintFacetVC
{
  public JStatement diagnoseByFacet(final DataTypeWithFacet datatype, final JCodeModel codeModel, final JDefinedClass theClass, final JExpression value, final JAssignmentTarget problem)
  {
    final JBlock block = newBlock();
    final EnumerationFacet enumerationFacet = (EnumerationFacet) datatype;
    final Set values = enumerationFacet.values;
    final String valuesFieldName = Util.generateFieldName(theClass, "values");
    final String valueSetFieldName = Util.generateFieldName(theClass, "valueSet");

    final JArray valuesArray = JExpr.newArray(codeModel.ref(Object.class));

    for (Iterator iterator = values.iterator(); iterator.hasNext();)
    {
      final Object currentValue = iterator.next();
      final JExpression currentValueExpression = create(datatype, codeModel, currentValue);
      valuesArray.add(currentValueExpression);
    }

    final JFieldVar valuesField = theClass.field(JMod.PROTECTED, codeModel.ref(Object.class).array(), valuesFieldName, valuesArray);
    final JFieldVar valueSetField =
      theClass.field(JMod.PROTECTED, codeModel.ref(Set.class),
        valueSetFieldName,
        codeModel.ref(Collections.class).staticInvoke("unmodifiableSet").arg
      (JExpr._new(codeModel.ref(HashSet.class)).arg(codeModel.ref(Arrays.class).staticInvoke("asList").arg(valuesField))));

    final JConditional ifEnumerationContainValue =
      block._if(valueSetField.invoke("contains").arg(value));

    ifEnumerationContainValue._then().directStatement("// Value is found in the enumeration, it is valid");
    ifEnumerationContainValue._else().
      assign(problem,
        JExpr._new(codeModel.ref(EnumerationProblem.class)).
      arg(value).arg(valueSetField));


    return block;
  }
}
