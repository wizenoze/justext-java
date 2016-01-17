package nl.wizenoze.justext.util

import nl.wizenoze.justext.exception.JusTextStopWordsException
import spock.lang.Specification

/**
 * Created by lcsontos on 1/17/16.
 */
class StopWordsUtilTest extends Specification {

    def testDutch() {
        when:
        Set<String> stopWords = StopWordsUtil.getStopWords("nl");

        then:
        !stopWords.isEmpty()
        stopWords.contains("zijn")
        stopWords.contains("voor")
    }

    def testEnglish() {
        when:
        Set<String> stopWords = StopWordsUtil.getStopWords("en");

        then:
        !stopWords.isEmpty()
        stopWords.contains("and")
        stopWords.contains("the")
    }

    def testGerman() {
        when:
        Set<String> stopWords = StopWordsUtil.getStopWords("de");

        then:
        !stopWords.isEmpty()
        stopWords.contains("und")
        stopWords.contains("größte")
    }

    def testHungarian() {
        when:
        Set<String> stopWords = StopWordsUtil.getStopWords("hu");

        then:
        !stopWords.isEmpty()
        stopWords.contains("és")
        stopWords.contains("az")
        stopWords.contains("ő")
    }

    def testNoSuchLanguageCode() {
        when:
        Set<String> stopWords = StopWordsUtil.getStopWords("xx");

        then:
        thrown(JusTextStopWordsException)
    }

}
