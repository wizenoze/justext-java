package nl.wizenoze.justext.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;

import static nl.wizenoze.justext.StringPool.CARRIAGE_RETURN;
import static nl.wizenoze.justext.StringPool.NEW_LINE;
import static nl.wizenoze.justext.StringPool.SPACE;

/**
 * @author László Csontos
 */
public final class TextUtil {

    private static final Pattern MULTIPLE_WHITESPACE_PATTERN = Pattern.compile("\\s+", UNICODE_CHARACTER_CLASS);

    private TextUtil() {
    }

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
