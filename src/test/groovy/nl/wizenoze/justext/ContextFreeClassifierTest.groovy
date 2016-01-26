package nl.wizenoze.justext

import nl.wizenoze.justext.paragraph.MutableParagraphImpl
import nl.wizenoze.justext.paragraph.PathInfo

import spock.lang.Specification

import static nl.wizenoze.justext.Classification.BAD
import static nl.wizenoze.justext.Classification.GOOD
import static nl.wizenoze.justext.Classification.NEAR_GOOD
import static nl.wizenoze.justext.Classification.SHORT

import static nl.wizenoze.justext.util.StringPool.COPYRIGHT_CHAR;
import static nl.wizenoze.justext.util.StringPool.COPYRIGHT_CODE;

/**
 * Created by lcsontos on 1/8/16.
 */
class ContextFreeClassifierTest extends Specification {

    def testCopyrightSymbol() {
        def paragraphs = [
                new MutableParagraphImpl(["blah blah ${COPYRIGHT_CHAR} blah blah"]),
                new MutableParagraphImpl(["blah blah ${COPYRIGHT_CODE} blah blah"]),
        ]

        when:
        Classifier.classifyContextFree(paragraphs, [] as Set)

        then:
        paragraphs[0].classification.equals(BAD)
        paragraphs[1].classification.equals(BAD)
    }

    def testLengthLow() {
        def paragraphs = [
                new MutableParagraphImpl(["0 1 2 3 4 5 6 7 8 9"] * 2, 0),
                new MutableParagraphImpl(["0 1 2 3 4 5 6 7 8 9"] * 2, 20),
        ]

        def classifierProperties = new ClassifierProperties.Builder()
                .setMaxLinkDensity(1)
                .setLengthLow(1000)
                .setLengthHigh(2000)
                .build()

        when:
        Classifier.classifyContextFree(paragraphs, [] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(SHORT)
        paragraphs[1].classification.equals(BAD)
    }

    def testMaxLinkDensity() {
        def paragraphs = [
                new MutableParagraphImpl(["0123456789"] * 2, 0),
                new MutableParagraphImpl(["0123456789"] * 2, 20),
                new MutableParagraphImpl(["0123456789"] * 8, 40),
                new MutableParagraphImpl(["0123456789"] * 8, 39),
                new MutableParagraphImpl(["0123456789"] * 8, 41),
        ]

        def classifierProperties = new ClassifierProperties.Builder().setMaxLinkDensity(0.5).build()

        when:
        Classifier.classifyContextFree(paragraphs, [] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(SHORT)
        paragraphs[1].classification.equals(BAD)
        paragraphs[2].classification.equals(BAD)
        paragraphs[3].classification.equals(BAD)
        paragraphs[4].classification.equals(BAD)
    }

    def testSelectTag() {
        def paragraphs = [
                new MutableParagraphImpl(
                        new PathInfo().append("html").append("body").append("select").append("option")),
                new MutableParagraphImpl(new PathInfo().append("select")),
        ]

        when:
        Classifier.classifyContextFree(paragraphs, [] as Set)

        then:
        paragraphs[0].classification.equals(BAD)
        paragraphs[1].classification.equals(BAD)
    }

    def testStopwordsHigh() {
        def paragraphs = [
                new MutableParagraphImpl(["0 1 2 3 4 5 6 7 8 9"]),
                new MutableParagraphImpl(["0 1 2 3 4 5 6 7 8 9"] * 2),
        ]

        def classifierProperties = new ClassifierProperties.Builder()
                .setMaxLinkDensity(1)
                .setLengthLow(0)
                .setLengthHigh(20)
                .setStopWordsLow(0)
                .setStopWordsHigh(0)
                .build()

        when:
        Classifier.classifyContextFree(paragraphs, ["0"] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(NEAR_GOOD)
        paragraphs[1].classification.equals(GOOD)
    }

    def testStopwordsLow() {
        def paragraphs = [
                new MutableParagraphImpl(["0 0 0 0 1 2 3 4 5 6 7 8 9"]),
                new MutableParagraphImpl(["0 1 2 3 4 5 6 7 8 9"]),
                new MutableParagraphImpl(["1 2 3 4 5 6 7 8 9"]),
        ]

        def classifierProperties = new ClassifierProperties.Builder()
                .setMaxLinkDensity(1)
                .setLengthLow(0)
                .setStopWordsLow(0.2)
                .setStopWordsHigh(1000)
                .build()

        when:
        Classifier.classifyContextFree(paragraphs, ["0", "1"] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(NEAR_GOOD)
        paragraphs[1].classification.equals(NEAR_GOOD)
        paragraphs[2].classification.equals(BAD)
    }

}
