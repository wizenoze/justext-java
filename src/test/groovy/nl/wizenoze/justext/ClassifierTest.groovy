package nl.wizenoze.justext

import spock.lang.Specification

import static nl.wizenoze.justext.Classification.BAD
import static nl.wizenoze.justext.Classification.GOOD
import static nl.wizenoze.justext.Classification.NEAR_GOOD
import static nl.wizenoze.justext.Classification.SHORT

/**
 * Created by lcsontos on 1/8/16.
 */
class ClassifierTest extends Specification {

    def testLengthLow() {
        def paragraphs = [
                new Paragraph(["0 1 2 3 4 5 6 7 8 9"] * 2, 0),
                new Paragraph(["0 1 2 3 4 5 6 7 8 9"] * 2, 20),
        ]

        def classifierProperties = new ClassifierProperties.Builder()
                .setMaxLinkDensity(1)
                .setLengthLow(1000)
                .build()

        when:
        Classifier.classifyParagraphs(paragraphs, [] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(SHORT)
        paragraphs[1].classification.equals(BAD)
    }

    def testMaxLinkDensity() {
        def paragraphs = [
                new Paragraph(["0123456789"] * 2, 0),
                new Paragraph(["0123456789"] * 2, 20),
                new Paragraph(["0123456789"] * 8, 40),
                new Paragraph(["0123456789"] * 8, 39),
                new Paragraph(["0123456789"] * 8, 41),
        ]

        def classifierProperties = new ClassifierProperties.Builder().setMaxLinkDensity(0.5).build()

        when:
        Classifier.classifyParagraphs(paragraphs, [] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(SHORT)
        paragraphs[1].classification.equals(BAD)
        paragraphs[2].classification.equals(BAD)
        paragraphs[3].classification.equals(BAD)
        paragraphs[4].classification.equals(BAD)
    }

    def testStopwordsHigh() {
        def paragraphs = [
                new Paragraph(["0 1 2 3 4 5 6 7 8 9"]),
                new Paragraph(["0 1 2 3 4 5 6 7 8 9"] * 2),
        ]

        def classifierProperties = new ClassifierProperties.Builder()
                .setMaxLinkDensity(1)
                .setLengthLow(0)
                .setLengthHigh(20)
                .setStopwordsHigh(0)
                .build()

        when:
        Classifier.classifyParagraphs(paragraphs, ["0"] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(NEAR_GOOD)
        paragraphs[1].classification.equals(GOOD)
    }

    def testStopwordsLow() {
        def paragraphs = [
                new Paragraph(["0 0 0 0 1 2 3 4 5 6 7 8 9"]),
                new Paragraph(["0 1 2 3 4 5 6 7 8 9"]),
                new Paragraph(["1 2 3 4 5 6 7 8 9"]),
        ]

        def classifierProperties = new ClassifierProperties.Builder()
                .setMaxLinkDensity(1)
                .setLengthLow(0)
                .setStopwordsLow(0.2)
                .setStopwordsHigh(1000)
                .build()

        when:
        Classifier.classifyParagraphs(paragraphs, ["0", "1"] as Set, classifierProperties)

        then:
        paragraphs[0].classification.equals(NEAR_GOOD)
        paragraphs[1].classification.equals(NEAR_GOOD)
        paragraphs[2].classification.equals(BAD)
    }

}
