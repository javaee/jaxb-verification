package de.fzi.dbs.verification.datatype;

import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.xsd.AnyURIType;
import com.sun.msv.datatype.xsd.Base64BinaryType;
import com.sun.msv.datatype.xsd.BooleanType;
import com.sun.msv.datatype.xsd.ByteType;
import com.sun.msv.datatype.xsd.DateTimeType;
import com.sun.msv.datatype.xsd.DateType;
import com.sun.msv.datatype.xsd.DoubleType;
import com.sun.msv.datatype.xsd.DurationType;
import com.sun.msv.datatype.xsd.EntityType;
import com.sun.msv.datatype.xsd.EnumerationFacet;
import com.sun.msv.datatype.xsd.ErrorType;
import com.sun.msv.datatype.xsd.FinalComponent;
import com.sun.msv.datatype.xsd.FloatType;
import com.sun.msv.datatype.xsd.FractionDigitsFacet;
import com.sun.msv.datatype.xsd.GDayType;
import com.sun.msv.datatype.xsd.GMonthDayType;
import com.sun.msv.datatype.xsd.GMonthType;
import com.sun.msv.datatype.xsd.GYearMonthType;
import com.sun.msv.datatype.xsd.GYearType;
import com.sun.msv.datatype.xsd.HexBinaryType;
import com.sun.msv.datatype.xsd.IDREFType;
import com.sun.msv.datatype.xsd.IDType;
import com.sun.msv.datatype.xsd.IntType;
import com.sun.msv.datatype.xsd.IntegerType;
import com.sun.msv.datatype.xsd.LanguageType;
import com.sun.msv.datatype.xsd.LengthFacet;
import com.sun.msv.datatype.xsd.ListType;
import com.sun.msv.datatype.xsd.LongType;
import com.sun.msv.datatype.xsd.MaxExclusiveFacet;
import com.sun.msv.datatype.xsd.MaxInclusiveFacet;
import com.sun.msv.datatype.xsd.MaxLengthFacet;
import com.sun.msv.datatype.xsd.MinExclusiveFacet;
import com.sun.msv.datatype.xsd.MinInclusiveFacet;
import com.sun.msv.datatype.xsd.MinLengthFacet;
import com.sun.msv.datatype.xsd.NameType;
import com.sun.msv.datatype.xsd.NcnameType;
import com.sun.msv.datatype.xsd.NegativeIntegerType;
import com.sun.msv.datatype.xsd.NmtokenType;
import com.sun.msv.datatype.xsd.NonNegativeIntegerType;
import com.sun.msv.datatype.xsd.NonPositiveIntegerType;
import com.sun.msv.datatype.xsd.NormalizedStringType;
import com.sun.msv.datatype.xsd.NumberType;
import com.sun.msv.datatype.xsd.PatternFacet;
import com.sun.msv.datatype.xsd.PositiveIntegerType;
import com.sun.msv.datatype.xsd.Proxy;
import com.sun.msv.datatype.xsd.QnameType;
import com.sun.msv.datatype.xsd.ShortType;
import com.sun.msv.datatype.xsd.SimpleURType;
import com.sun.msv.datatype.xsd.StringType;
import com.sun.msv.datatype.xsd.TimeType;
import com.sun.msv.datatype.xsd.TokenType;
import com.sun.msv.datatype.xsd.TotalDigitsFacet;
import com.sun.msv.datatype.xsd.UnsignedByteType;
import com.sun.msv.datatype.xsd.UnsignedIntType;
import com.sun.msv.datatype.xsd.UnsignedLongType;
import com.sun.msv.datatype.xsd.UnsignedShortType;
import com.sun.msv.datatype.xsd.WhiteSpaceFacet;
import de.fzi.dbs.verification.datatype.atomic.AnyURITypeVC;
import de.fzi.dbs.verification.datatype.atomic.BooleanTypeVC;
import de.fzi.dbs.verification.datatype.atomic.EntityTypeVC;
import de.fzi.dbs.verification.datatype.atomic.ErrorTypeVC;
import de.fzi.dbs.verification.datatype.atomic.QnameTypeVC;
import de.fzi.dbs.verification.datatype.atomic.SimpleURTypeVC;
import de.fzi.dbs.verification.datatype.atomic.binary.Base64BinaryTypeVC;
import de.fzi.dbs.verification.datatype.atomic.binary.HexBinaryTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.DateTimeTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.DateTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.DurationTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.GDayTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.GMonthDayTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.GMonthTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.GYearMonthTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.GYearTypeVC;
import de.fzi.dbs.verification.datatype.atomic.datetime.TimeTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.ByteTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.DoubleTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.FloatTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.IntTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.IntegerTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.LongTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.NegativeIntegerTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.NonNegativeIntegerTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.NonPositiveIntegerTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.NumberTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.PositiveIntegerTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.UnsignedByteTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.UnsignedIntTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.UnsignedLongTypeVC;
import de.fzi.dbs.verification.datatype.atomic.number.UnsignedShortTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.IDREFTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.IDTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.LanguageTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.NameTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.NcnameTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.NmtokenTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.NormalizedStringTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.StringTypeVC;
import de.fzi.dbs.verification.datatype.atomic.string.TokenTypeVC;
import de.fzi.dbs.verification.datatype.facet.lexical.FractionDigitsFacetVC;
import de.fzi.dbs.verification.datatype.facet.lexical.PatternFacetVC;
import de.fzi.dbs.verification.datatype.facet.lexical.TotalDigitsFacetVC;
import de.fzi.dbs.verification.datatype.facet.lexical.WhiteSpaceFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.EnumerationFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.LengthFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.MaxExclusiveFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.MaxInclusiveFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.MaxLengthFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.MinExclusiveFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.MinInclusiveFacetVC;
import de.fzi.dbs.verification.datatype.facet.value.MinLengthFacetVC;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory of verificator constructors.
 */
public class VerificatorConstructorFactory
{
  /**
   * Logger.
   */
  protected static Log log = LogFactory.getLog(VerificatorConstructorFactory.class);
  /**
   * Map of VCs.
   */
  protected static Map vcs = new HashMap();

  static
  {
    vcs.put(Proxy.class, ProxyVC.class);
    vcs.put(FinalComponent.class, FinalComponentVC.class);
    vcs.put(ListType.class, ListTypeVC.class);

    /// Atomic

    // Binary
    vcs.put(Base64BinaryType.class, Base64BinaryTypeVC.class);
    vcs.put(HexBinaryType.class, HexBinaryTypeVC.class);

    // DateTime
    vcs.put(DateTimeType.class, DateTimeTypeVC.class);
    vcs.put(DateType.class, DateTypeVC.class);
    vcs.put(TimeType.class, TimeTypeVC.class);
    vcs.put(DurationType.class, DurationTypeVC.class);
    vcs.put(GDayType.class, GDayTypeVC.class);
    vcs.put(GMonthDayType.class, GMonthDayTypeVC.class);
    vcs.put(GMonthType.class, GMonthTypeVC.class);
    vcs.put(GYearMonthType.class, GYearMonthTypeVC.class);
    vcs.put(GYearType.class, GYearTypeVC.class);

    // Number
    vcs.put(IntegerType.class, IntegerTypeVC.class);
    vcs.put(NonNegativeIntegerType.class, NonNegativeIntegerTypeVC.class);
    vcs.put(NegativeIntegerType.class, NegativeIntegerTypeVC.class);
    vcs.put(NonPositiveIntegerType.class, NonPositiveIntegerTypeVC.class);
    vcs.put(PositiveIntegerType.class, PositiveIntegerTypeVC.class);
    vcs.put(LongType.class, LongTypeVC.class);
    vcs.put(IntType.class, IntTypeVC.class);
    vcs.put(ShortType.class, de.fzi.dbs.verification.datatype.atomic.number.ShortTypeVC.class);
    vcs.put(ByteType.class, ByteTypeVC.class);
    vcs.put(UnsignedLongType.class, UnsignedLongTypeVC.class);
    vcs.put(UnsignedIntType.class, UnsignedIntTypeVC.class);
    vcs.put(UnsignedShortType.class, UnsignedShortTypeVC.class);
    vcs.put(UnsignedByteType.class, UnsignedByteTypeVC.class);
    vcs.put(NumberType.class, NumberTypeVC.class);
    vcs.put(FloatType.class, FloatTypeVC.class);
    vcs.put(DoubleType.class, DoubleTypeVC.class);

    // String
    vcs.put(IDType.class, IDTypeVC.class);
    vcs.put(IDREFType.class, IDREFTypeVC.class);
    vcs.put(StringType.class, StringTypeVC.class);
    vcs.put(NormalizedStringType.class, NormalizedStringTypeVC.class);
    vcs.put(TokenType.class, TokenTypeVC.class);
    vcs.put(NmtokenType.class, NmtokenTypeVC.class);
    vcs.put(NameType.class, NameTypeVC.class);
    vcs.put(NcnameType.class, NcnameTypeVC.class);
    vcs.put(LanguageType.class, LanguageTypeVC.class);

    // Other atomic
    vcs.put(AnyURIType.class, AnyURITypeVC.class);
    vcs.put(BooleanType.class, BooleanTypeVC.class);
    vcs.put(EntityType.class, EntityTypeVC.class);
    vcs.put(ErrorType.class, ErrorTypeVC.class);
    vcs.put(QnameType.class, QnameTypeVC.class);
    vcs.put(SimpleURType.class, SimpleURTypeVC.class);

    /// Facets
    vcs.put(FractionDigitsFacet.class, FractionDigitsFacetVC.class);
    vcs.put(PatternFacet.class, PatternFacetVC.class);
    vcs.put(TotalDigitsFacet.class, TotalDigitsFacetVC.class);
    vcs.put(WhiteSpaceFacet.class, WhiteSpaceFacetVC.class);

    vcs.put(LengthFacet.class, LengthFacetVC.class);
    vcs.put(MinLengthFacet.class, MinLengthFacetVC.class);
    vcs.put(MaxLengthFacet.class, MaxLengthFacetVC.class);

    vcs.put(MinInclusiveFacet.class, MinInclusiveFacetVC.class);
    vcs.put(MinExclusiveFacet.class, MinExclusiveFacetVC.class);
    vcs.put(MaxInclusiveFacet.class, MaxInclusiveFacetVC.class);
    vcs.put(MaxExclusiveFacet.class, MaxExclusiveFacetVC.class);

    vcs.put(EnumerationFacet.class, EnumerationFacetVC.class);
  }

  /**
   * Returns verificator constructor for the given datatype or <code>null</code> if it is not defined.
   *
   * @param datatype datatype.
   * @return VC for the given datatype of <code>null</code> as fallback value.
   */
  public static VerificatorConstructor getVerificatorConstructor(final DatabindableDatatype datatype)
  {
    final Class theClass = (Class) vcs.get(datatype.getClass());
    if (null != theClass)
    {
      try
      {
        return (VerificatorConstructor) theClass.newInstance();
      }
      catch (IllegalAccessException iaex)
      {
        // todo
        return null;
      }
      catch (InstantiationException iex)
      {
        // todo
        return null;
      }
    }
    else
    {
      // todo
      log.warn("No VC found for the datatype [" + datatype.getClass() + "].");
      return null;
    }
  }
}
