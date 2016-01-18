package nl.wizenoze.justext.util

import spock.lang.Specification

/**
 * Created by lcsontos on 1/12/16.
 */
class StringUtilTest extends Specification {

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

    def test_normalize_non_break_spaces() {
        def string = "\u00A0\t €\u202F \t"
        def expected = " € "

        when:
        def normalized = StringUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

}
