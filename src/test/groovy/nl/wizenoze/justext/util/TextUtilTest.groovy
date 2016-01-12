package nl.wizenoze.justext.util

import spock.lang.Specification

/**
 * Created by lcsontos on 1/12/16.
 */
class TextUtilTest extends Specification {

    def testNormalizeNoChange() {
        when:
        def string = "a b c d e f g h i j k l m n o p q r s ..."

        then:
        TextUtil.normalizeWhiteSpaces(string) == string
    }

    def testNormalizeDontTrim() {
        def string = "  a b c d e f g h i j k l m n o p q r s ...  "
        def expected = " a b c d e f g h i j k l m n o p q r s ... "

        when:
        def normalized = TextUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

    def testNormalizeNewlineAndTab() {
        def string = "123 \n456\t\n"
        def expected = "123\n456\n"

        when:
        def normalized = TextUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

    def test_normalize_non_break_spaces() {
        def string = "\u00A0\t €\u202F \t"
        def expected = " € "

        when:
        def normalized = TextUtil.normalizeWhiteSpaces(string)

        then:
        expected == normalized
    }

}
