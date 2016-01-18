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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;

import static nl.wizenoze.justext.util.StringPool.CARRIAGE_RETURN;
import static nl.wizenoze.justext.util.StringPool.NEW_LINE;
import static nl.wizenoze.justext.util.StringPool.SPACE;

/**
 * @author László Csontos
 */
public final class StringUtil {

    private static final Pattern MULTIPLE_WHITESPACE_PATTERN = Pattern.compile("\\s+", UNICODE_CHARACTER_CLASS);

    private StringUtil() {
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

            if (StringUtils.contains(token, CARRIAGE_RETURN) || StringUtils.contains(token, NEW_LINE)) {
                matcher.appendReplacement(stringBuffer, NEW_LINE);
            } else {
                matcher.appendReplacement(stringBuffer, SPACE);
            }
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

}
