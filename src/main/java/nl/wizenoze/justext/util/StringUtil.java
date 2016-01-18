/**
 * Copyright (c) 2016-present WizeNoze B.V. All rights reserved.
 *
 * This file is part of justext-java.
 *
 * justext-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * justext-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with justext-java.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.wizenoze.justext.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;

import static nl.wizenoze.justext.util.StringPool.CARRIAGE_RETURN;
import static nl.wizenoze.justext.util.StringPool.NEW_LINE;
import static nl.wizenoze.justext.util.StringPool.SPACE;

/**
 * @author László Csontos
 */
public final class StringUtil {

    private static final Pattern MULTIPLE_WHITESPACE_PATTERN = Pattern.compile("\\s+", UNICODE_CHARACTER_CLASS);
    private static final Set<Character> UNICODE_WHITESPACE_CHARACTERS = new HashSet<>(17);

    static {
        // http://www.fileformat.info/info/unicode/category/Zs/list.htm

        UNICODE_WHITESPACE_CHARACTERS.add('\u0020');    // SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u00A0');    // NO-BREAK SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u1680');    // OGHAM SPACE MARK
        UNICODE_WHITESPACE_CHARACTERS.add('\u2000');    // EN QUAD
        UNICODE_WHITESPACE_CHARACTERS.add('\u2001');    // EM QUAD
        UNICODE_WHITESPACE_CHARACTERS.add('\u2002');    // EN SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2003');    // EM SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2004');    // THREE-PER-EM SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2005');    // FOUR-PER-EM SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2006');    // SIX-PER-EM SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2007');    // FIGURE SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2008');    // PUNCTUATION SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u2009');    // THIN SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u200A');    // HAIR SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u202F');    // NARROW NO-BREAK SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u205F');    // MEDIUM MATHEMATICAL SPACE
        UNICODE_WHITESPACE_CHARACTERS.add('\u3000');    // IDEOGRAPHIC SPACE
    }

    private StringUtil() {
    }

    /**
     * Returns true if the given string contains the specified token.
     *
     * @param string string.
     * @param token token.
     * @return true if the given string contains the specified token.
     */
    public static boolean contains(String string, char token) {
        if (isEmpty(string)) {
            return false;
        }

        return (string.indexOf(token) != -1);
    }

    /**
     * Returns true if the given string contains the specified token.
     *
     * @param string string.
     * @param token token.
     * @return true if the given string contains the specified token.
     */
    public static boolean contains(String string, String token) {
        if (isEmpty(string)) {
            return false;
        }

        return (string.indexOf(token) != -1);
    }

    /**
     * Returns if the given string is blank, meaning that it's either {@link #isEmpty(String)} or contains only white
     * space characters.
     *
     * @param string string to check.
     * @return if it's blank or not.
     */
    public static boolean isBlank(String string) {
        if (isEmpty(string)) {
            return true;
        }

        for (int index = 0; index < string.length(); index++) {
            char codePoint = string.charAt(index);

            if (UNICODE_WHITESPACE_CHARACTERS.contains(codePoint) || Character.isWhitespace(codePoint)) {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Returns if the given string is empty, meaning that it's either <code>null</code> or an empty string.
     *
     * @param string string to check.
     * @return if it's empty or not.
     */
    public static boolean isEmpty(String string) {
        if ((string == null) || string.length() == 0) {
            return true;
        }

        return false;
    }

    /**
     * Returns the opposite of {@link #isBlank(String)}.
     *
     * @param string string to check.
     * @return if it isn't blank.
     */
    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    /**
     * Returns the opposite of {@link #isEmpty(String)}.
     *
     * @param string string to check.
     * @return if it isn't empty.
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    /**
     * Translates multiple whitespace into single space character. If there is at least one new line character chunk is
     * replaced by single LF (Unix new line) character.
     * @param text text to be normalized.
     * @return normalized text.
     */
    public static String normalizeWhiteSpaces(String text) {
        Matcher matcher = MULTIPLE_WHITESPACE_PATTERN.matcher(text);

        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            String token = matcher.group();

            if (contains(token, CARRIAGE_RETURN) || contains(token, NEW_LINE)) {
                matcher.appendReplacement(stringBuffer, NEW_LINE);
            } else {
                matcher.appendReplacement(stringBuffer, SPACE);
            }
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

}
