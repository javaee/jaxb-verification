package de.fzi.dbs.verification.util;

import com.sun.msv.datatype.xsd.regex.RegularExpression;

/**
 * Validation utilities.
 *
 * @author Aleksei Valikov
 */
public class ValidationUtils
{
  /**
   * Hidden constructor.
   */
  private ValidationUtils()
  {
  }

  private static void appendHex(final StringBuffer buf, final int hex)
  {
    if (hex < 10)
    {
      buf.append((char) (hex + '0'));
    }
    else
    {
      buf.append((char) (hex - 10 + 'A'));
    }
  }

  private static void appendByte(final StringBuffer buf, final int ch)
  {
    buf.append('%');
    appendHex(buf, ch / 16);
    appendHex(buf, ch % 16);
  }

  private static void appendEscaped(final StringBuffer buf, final char ch)
  {
    if (ch < 0x7F)
    {
      appendByte(buf, (int) ch);
      return;
    }

    if (ch < 0x7FF)
    {
      appendByte(buf, 0xC0 + (ch >> 6));
      appendByte(buf, 0x80 + (ch % 64));
      return;
    }

    if (ch < 0xFFFF)
    {
      appendByte(buf, 0xE0 + (ch >> 12));
      appendByte(buf, 0x80 + ((ch >> 6) % 64));
      appendByte(buf, 0x80 + (ch % 64));
    }
  }

  private static void appendEscaped(final StringBuffer buf, final char ch1, final char ch2)
  {
    final int ucs = ((ch1 & 0x3FF) << 10) + (ch2 & 0x3FF);

    appendByte(buf, 0xF0 + (ucs >> 18));
    appendByte(buf, 0x80 + ((ucs >> 12) % 64));
    appendByte(buf, 0x80 + ((ucs >> 6) % 64));
    appendByte(buf, 0x80 + (ucs % 64));
  }

  /**
   * a table that indicates whether a particular character has to be
   * escaped or not. false indicates it has to be escaped.
   * this table is of length 128.
   */
  private static final boolean[] isUric = createUricMap();

  private static boolean[] createUricMap()
  {
    final boolean[] r = new boolean[128];

    for (int i = 'a'; i <= 'z'; i++)
    {
      r[i] = true;
    }
    for (int i = 'A'; i <= 'Z'; i++)
    {
      r[i] = true;
    }
    for (int i = '0'; i <= '9'; i++)
    {
      r[i] = true;
    }

    final char[] mark = new char[]{'-', '_', '.', '!', '~', '*', '\'', '(', ')', '#', '%', '[', ']'};
    for (int i = 0; i < mark.length; i++)
    {
      r[mark[i]] = true;
    }

    final char[] reserved = new char[]{';', '/', '?', ':', '@', '&', '=', '+', '$', ','};
    for (int i = 0; i < reserved.length; i++)
    {
      r[reserved[i]] = true;
    }

    return r;
  }

  /**
   * Escapes the string.
   *
   * @param content string to escape.
   * @return Escaped string.
   */
  public static String escape(final String content)
  {
    final StringBuffer escaped = new StringBuffer(content.length());

    for (int i = 0; i < content.length(); i++)
    {
      final char ch = content.charAt(i);
      if (ch < 128 && isUric[ch])
      {
        escaped.append(ch);
      }
      else
      {
        // escape it
        if (0xD800 <= ch && ch < 0xDC00)	// surrogate pair
        {
          appendEscaped(escaped, ch, content.charAt(++i));
        }
        else	// other characters.
        {
          appendEscaped(escaped, ch);
        }
      }
    }
    return new String(escaped);
  }

  final static RegularExpression regexp = createRegExp();

  static RegularExpression createRegExp()
  {
    final String alpha = "[a-zA-Z]";
    final String alphanum = "[0-9a-zA-Z]";
    final String hex = "[0-9a-fA-F]";
    final String escaped = "%" + hex + "{2}";
    final String mark = "[\\-_\\.!~\\*'\\(\\)]";
    final String unreserved = "(" + alphanum + "|" + mark + ")";
    final String reserved = "[;/\\?:@&=\\+$,\\[\\]]";
    final String uric = "(" + reserved + "|" + unreserved + "|" + escaped + ")";
    final String fragment = uric + "*";
    final String query = uric + "*";
    final String pchar = "(" + unreserved + "|" + escaped + "|[:@&=\\+$,])";
    final String param = pchar + "*";
    final String segment = "(" + param + "(;" + param + ")*)";
    final String pathSegments = "(" + segment + "(/" + segment + ")*)";
    final String port = "[0-9]*";
    final String __upTo3digits = "[0-9]{1,3}";
    final String IPv4address = __upTo3digits + "\\." + __upTo3digits + "\\." + __upTo3digits + "\\." + __upTo3digits;
    final String hex4 = hex + "{1,4}";
    final String hexseq = hex4 + "(:" + hex4 + ")*";
    final String hexpart = "((" + hexseq + "(::(" + hexseq + ")?)?)|(::(" + hexseq + ")?))";
    final String IPv6address = "((" + hexpart + "(:" + IPv4address + ")?)|(::" + IPv4address + "))";
    final String IPv6reference = "\\[" + IPv6address + "\\]";
    final String domainlabel = alphanum + "([0-9A-Za-z\\-]*" + alphanum + ")?";
    final String toplabel = alpha + "([0-9A-Za-z\\-]*" + alphanum + ")?";
    final String hostname = "(" + domainlabel + "\\.)*" + toplabel + "(\\.)?";
    final String host = "((" + hostname + ")|(" + IPv4address + ")|(" + IPv6reference + "))";
    final String hostport = host + "(:" + port + ")?";
    final String userinfo = "(" + unreserved + "|" + escaped + "|[;:&=\\+$,])*";
    final String server = "((" + userinfo + "@)?" + hostport + ")?";
    final String regName = "(" + unreserved + "|" + escaped + "|[$,;:@&=\\+])+";
    final String authority = "((" + server + ")|(" + regName + "))";
    final String scheme = alpha + "[A-Za-z0-9\\+\\-\\.]*";
    final String relSegment = "(" + unreserved + "|" + escaped + "|[;@&=\\+$,])+";
    final String absPath = "/" + pathSegments;
    final String relPath = relSegment + "(" + absPath + ")?";
    final String netPath = "//" + authority + "(" + absPath + ")?";
    final String uricNoSlash = "(" + unreserved + "|" + escaped + "|[;\\?:@&=\\+$,])";
    final String opaquePart = uricNoSlash + "(" + uric + ")*";
    final String hierPart = "((" + netPath + ")|(" + absPath + "))(\\?" + query + ")?";
//    final String path = "((" + absPath + ")|(" + opaquePart + "))?";
    final String relativeURI = "((" + netPath + ")|(" + absPath + ")|(" + relPath + "))(\\?" + query + ")?";
    final String absoluteURI = scheme + ":((" + hierPart + ")|(" + opaquePart + "))";
    final String uriRef = "(" + absoluteURI + "|" + relativeURI + ")?(#" + fragment + ")?";
    return new RegularExpression(uriRef, "X");
  }

  /**
   * Checks if value is a valid anyURI.
   *
   * @param value value to check.
   * @return <code>true</code>, if value is a valid anyURI, <code>false</code> otherwise.
   */
  public static boolean isAnyURI(final String value)
  {
    return regexp.matches(escape(value));
  }

  /**
   * Checks if value is a valid language.
   *
   * @param value value to check.
   * @return <code>true</code>, if value is a valid language, <code>false</code> otherwise.
   */
  public static boolean isLanguage(final String value)
  {
    final int len = value.length();
    int i = 0;
    int tokenSize = 0;

    while (i < len)
    {
      final char ch = value.charAt(i++);
      if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))
      {
        tokenSize++;
        if (tokenSize == 9)
        {
          return false;
        }
      }
      else if (ch == '-')
      {
        if (tokenSize == 0)
        {
          return false;
        }
        tokenSize = 0;
      }
      else
      {
        return false;
      }
    }
    if (tokenSize == 0)
    {
      return false;
    }
    return true;
  }
}
