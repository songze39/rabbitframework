package com.rabbitframework.commons.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import com.rabbitframework.commons.exceptions.CodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    public final static String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
    public static final String PREFERRED_ENCODING = "UTF-8";

    public static int stringToInt(String value, int defaultValue) {
        int result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static Integer integerValueOf(String value, Integer defaultValue) {
        Integer result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        return Integer.valueOf(value);
    }

    public static long stringToLong(String value, long defaultValue) {
        Long result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean stringToBoolean(String value, boolean defaultValue) {
        boolean result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * 判断字符串是否为空串!
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空!
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为是空格串
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(cs.charAt(i)))
                return false;

        return true;
    }

    /**
     * 判断字符串是否不是空格串
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 去掉字符串空格
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    /**
     * 去掉空格为空时，转换成null
     *
     * @param str
     * @return
     */
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    /**
     * 去掉空格为空时，转换成空
     *
     * @param str
     * @return
     */
    public static String trimToEmpty(String str) {
        return str != null ? str.trim() : "";
    }

    /**
     * 数组转字符
     *
     * @param array
     * @param separator
     * @return
     */
    public static String ArrayToString(String[] array, String separator) {
        String str = "";

        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i < array.length - 1)
                    str += array[i] + separator;
                else
                    str += array[i];
            }
        }

        return str;
    }

    public static String IntegerToString(int a) {

        String Leverid = "";
        for (int i = 0; i < a; i++) {
            Leverid = Leverid + String.valueOf(i + 1) + ",";
        }

        return Leverid;

    }

    public static boolean compareString(String strA, String strB,
                                        String separator) {
        boolean flag = false;

        if (strA == null || strB == null)
            return flag;

        String[] strArrayA = strA == null ? new String[]{} : strA
                .split(separator);
        strB = separator + strB + separator;

        for (int i = 0; i < strArrayA.length; i++) {
            if (strB.indexOf(separator + strArrayA[i] + separator) > -1)
                flag = true;
        }

        return flag;
    }

    public static List<String> tokenizeToArray(String str) {
        return tokenizeToArray(str, CONFIG_LOCATION_DELIMITERS, true, false);
    }

    public static String[] tokenizeToStringArray(String str) {
        List<String> token = tokenizeToArray(str);
        return toStringArray(token);
    }

    public static List<String> tokenizeToArray(String str, String delimiters,
                                               boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * 字符串解析转换字符串数组
     *
     * @param str
     * @param delimiters
     * @param trimTokens        //是否去除空
     * @param ignoreEmptyTokens 是否忽略空
     * @return
     */
    public static String[] tokenizeToStringArray(String str, String delimiters,
                                                 boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        }
        List<String> tokens = tokenizeToArray(str, delimiters, trimTokens,
                ignoreEmptyTokens);
        return toStringArray(tokens);
    }

    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    public static byte[] toBytes(String source) {
        return toBytes(source, PREFERRED_ENCODING);
    }

    /**
     * Converts the specified byte array to a String using the {@link com.rabbitframework.commons.codec.CodecSupport#PREFERRED_ENCODING PREFERRED_ENCODING}.
     *
     * @param bytes the byte array to turn into a String.
     * @return the specified byte array as an encoded String ({@link com.rabbitframework.commons.codec.CodecSupport#PREFERRED_ENCODING PREFERRED_ENCODING}).
     * @see #toString(byte[], String)
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, PREFERRED_ENCODING);
    }

    /**
     * Converts the specified source to a byte array via the specified encoding, throwing a
     * {@link CodecException CodecException} if the encoding fails.
     *
     * @param source   the source string to convert to a byte array.
     * @param encoding the encoding to use to use.
     * @return the byte array of the specified source with the given encoding.
     * @throws CodecException if the JVM does not support the specified encoding.
     */
    public static byte[] toBytes(String source, String encoding) throws CodecException {
        try {
            return source.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            String msg = "Unable to convert source [" + source + "] to byte array using " +
                    "encoding '" + encoding + "'";
            throw new CodecException(msg, e);
        }
    }

    /**
     * Converts the specified byte array to a String using the specified character encoding.  This implementation
     * does the same thing as <code>new {@link String#String(byte[], String) String(byte[], encoding)}</code>, but will
     * wrap any {@link UnsupportedEncodingException} with a nicer runtime {@link CodecException}, allowing you to
     * decide whether or not you want to catch the exception or let it propagate.
     *
     * @param bytes    the byte array to convert to a String
     * @param encoding the character encoding used to encode the String.
     * @return the specified byte array as an encoded String
     * @throws CodecException if the JVM does not support the specified encoding.
     */
    public static String toString(byte[] bytes, String encoding) throws CodecException {
        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            String msg = "Unable to convert byte array to String with encoding '" + encoding + "'.";
            throw new CodecException(msg, e);
        }
    }

}
