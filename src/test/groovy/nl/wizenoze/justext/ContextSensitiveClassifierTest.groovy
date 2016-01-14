package nl.wizenoze.justext

import nl.wizenoze.justext.paragraph.Paragraph

import spock.lang.Specification

import static nl.wizenoze.justext.Classification.BAD
import static nl.wizenoze.justext.Classification.GOOD
import static nl.wizenoze.justext.Classification.NEAR_GOOD
import static nl.wizenoze.justext.Classification.SHORT

/**
 * Created by lcsontos on 1/13/16.
 */
class ContextSensitiveClassifierTest extends Specification {

    void assertClassification(Paragraph paragraph, Classification classification) {
        assert paragraph.classification.equals(classification)
    }

    def testBadAndGoodRemain() {
        def paragraphs = [
                new Paragraph(BAD),
                new Paragraph(BAD),
                new Paragraph(BAD),
                new Paragraph(BAD),
                new Paragraph(GOOD),
                new Paragraph(BAD),
                new Paragraph(GOOD),
                new Paragraph(BAD),
                new Paragraph(GOOD),
                new Paragraph(GOOD),
                new Paragraph(GOOD),
                new Paragraph(GOOD)
        ]

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        assertClassification(paragraphs[0], BAD)
        assertClassification(paragraphs[1], BAD)
        assertClassification(paragraphs[2], BAD)
        assertClassification(paragraphs[3], BAD)
        assertClassification(paragraphs[4], GOOD)
        assertClassification(paragraphs[5], BAD)
        assertClassification(paragraphs[6], GOOD)
        assertClassification(paragraphs[7], BAD)
        assertClassification(paragraphs[8], GOOD)
        assertClassification(paragraphs[9], GOOD)
        assertClassification(paragraphs[10], GOOD)
        assertClassification(paragraphs[11], GOOD)
    }

    def testBadAndNearGoodBetweenBadAndGood() {
        def paragraphs = [
                new Paragraph(BAD),
                new Paragraph(SHORT),
                new Paragraph(NEAR_GOOD),
                new Paragraph(SHORT),
                new Paragraph(SHORT),
                new Paragraph(GOOD)
        ]

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        assertClassification(paragraphs[0], BAD)
        assertClassification(paragraphs[1], BAD)
        assertClassification(paragraphs[2], GOOD)
        assertClassification(paragraphs[3], GOOD)
        assertClassification(paragraphs[4], GOOD)
        assertClassification(paragraphs[5], GOOD)
    }

    def testBadAndNearGoodBetweenGoodAndBad() {
        def paragraphs = [
                new Paragraph(GOOD),
                new Paragraph(SHORT),
                new Paragraph(NEAR_GOOD),
                new Paragraph(SHORT),
                new Paragraph(SHORT),
                new Paragraph(BAD)
        ]

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        assertClassification(paragraphs[0], GOOD)
        assertClassification(paragraphs[1], GOOD)
        assertClassification(paragraphs[2], GOOD)
        assertClassification(paragraphs[3], BAD)
        assertClassification(paragraphs[4], BAD)
        assertClassification(paragraphs[5], BAD)
    }

    def testBadAndNearGoodBetweenTwoBad() {
        def paragraphs = [
                new Paragraph(BAD),
                new Paragraph(SHORT),
                new Paragraph(NEAR_GOOD),
                new Paragraph(BAD)
        ]

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        assertClassification(paragraphs[0], BAD)
        assertClassification(paragraphs[1], BAD)
        assertClassification(paragraphs[2], BAD)
        assertClassification(paragraphs[3], BAD)
    }

    def testBadAndNearGoodBetweenTwoGood() {
        def paragraphs = [
                new Paragraph(GOOD),
                new Paragraph(SHORT),
                new Paragraph(NEAR_GOOD),
                new Paragraph(GOOD)
        ]

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        assertClassification(paragraphs[0], GOOD)
        assertClassification(paragraphs[1], GOOD)
        assertClassification(paragraphs[2], GOOD)
        assertClassification(paragraphs[3], GOOD)
    }

    /*
     * http://corpus.tools/attachment/wiki/Justext/Algorithm/cs_classification_example.png
     */
    def testComplexExample() {
        def paragraphs = [
                new Paragraph(BAD),
                new Paragraph(SHORT),
                new Paragraph(BAD),
                new Paragraph(SHORT),
                new Paragraph(NEAR_GOOD),
                new Paragraph(BAD),
                new Paragraph(SHORT),
                new Paragraph(SHORT),
                new Paragraph(GOOD),
                new Paragraph(SHORT),
                new Paragraph(GOOD),
                new Paragraph(SHORT),
                new Paragraph(NEAR_GOOD),
                new Paragraph(SHORT),
                new Paragraph(BAD),
                new Paragraph(NEAR_GOOD)
        ]

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        assertClassification(paragraphs[0], BAD)
        assertClassification(paragraphs[1], BAD)
        assertClassification(paragraphs[2], BAD)
        assertClassification(paragraphs[3], BAD)
        assertClassification(paragraphs[4], BAD)
        assertClassification(paragraphs[5], BAD)
        assertClassification(paragraphs[6], BAD)
        assertClassification(paragraphs[7], BAD)
        assertClassification(paragraphs[8], GOOD)
        assertClassification(paragraphs[9], GOOD)
        assertClassification(paragraphs[10], GOOD)
        assertClassification(paragraphs[11], GOOD)
        assertClassification(paragraphs[12], GOOD)
        assertClassification(paragraphs[13], BAD)
        assertClassification(paragraphs[14], BAD)
        assertClassification(paragraphs[15], BAD)
    }

    def testEmpty() {
        def paragraphs = []

        when:
        Classifier.reviseParagraphs(paragraphs)

        then:
        paragraphs.isEmpty()
    }

}
