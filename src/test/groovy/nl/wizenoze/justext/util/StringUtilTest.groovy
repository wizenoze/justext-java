package nl.wizenoze.justext.util

import spock.lang.Specification

/**
 * Created by lcsontos on 1/12/16.
 */
class StringUtilTest extends Specification {

    def testIsBlankWithNull() {
        when:
        boolean blank = StringUtil.isBlank(null)

        then:
        blank
    }

    def testIsBlankWithNormalText() {
        when:
        boolean blank = StringUtil.isBlank(" a b c d e f g h i j k l m n o p q r s ...")

        then:
        !blank
    }

    def testIsBlankWithWhiteSpace() {
        when:
        boolean blank = StringUtil.isBlank(" \t\n\r\u00a0\u202f")

        then:
        blank
    }

    def testIsEmptyWithNull() {
        when:
        boolean empty = StringUtil.isEmpty(null)

        then:
        empty
    }

    def testIsEmptyWithNormalText() {
        when:
        boolean empty = StringUtil.isEmpty(" a b c d e f g h i j k l m n o p q r s ...")

        then:
        !empty
    }

    def testIsEmptyWithWhiteSpace() {
        when:
        boolean empty = StringUtil.isEmpty(" \t\n\r\u00a0\u202f")

        then:
        !empty
    }

    def testNormalizeNoChange() {
        when:
        def string = "a b c d e f g h i j k l m n o p q r s ..."

        then:
        StringUtil.normalizeWhiteSpaces(string) == string
    }

    def testNormalizeDontTrim() {
        def string = "  a b c d e f g h i j k l m n o p q r s ...  "
        def expected = " a b c d e f g h i j k l m n o p q r s ... "

        when:
        def normalized = StringUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

    def testNormalizeNewlineAndTab() {
        def string = "123 \n456\t\n"
        def expected = "123\n456\n"

        when:
        def normalized = StringUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

    def testNormalizeNonBreakSpaces() {
        def string = "\u00A0\t €\u202F \t"
        def expected = " € "

        when:
        def normalized = StringUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

}
