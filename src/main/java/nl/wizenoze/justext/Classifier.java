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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.wizenoze.justext;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;

import nl.wizenoze.justext.paragraph.MutableParagraph;
import nl.wizenoze.justext.paragraph.Paragraph;

import org.apache.commons.lang3.StringUtils;

import static nl.wizenoze.justext.Classification.BAD;
import static nl.wizenoze.justext.Classification.GOOD;
import static nl.wizenoze.justext.Classification.NEAR_GOOD;
import static nl.wizenoze.justext.Classification.SHORT;

import static nl.wizenoze.justext.util.StringPool.COPYRIGHT_CHAR;
import static nl.wizenoze.justext.util.StringPool.COPYRIGHT_CODE;

/**
 * This is the implementation of the following context free classification algorithm.
 *
 * @author László Csontos
 * @see http://corpus.tools/wiki/Justext/Algorithm
 */
public final class Classifier {

    private static final Set<Classification> BAD_SET = EnumSet.of(BAD);
    private static final Set<Classification> BAD_GOOD_SET = EnumSet.of(BAD, GOOD);
    private static final Set<Classification> BAD_GOOD_NEAR_GOOD_SET = EnumSet.of(BAD, GOOD, NEAR_GOOD);
    private static final Set<Classification> GOOD_SET = EnumSet.of(GOOD);
    private static final ClassifierProperties CLASSIFIER_PROPERTIES_DEFAULT = ClassifierProperties.getDefault();
    private static final Pattern SELECT_PATTERN = Pattern.compile("^select|\\.select");

    private Classifier() {
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     */
    public static void classifyParagraphs(List<MutableParagraph> paragraphs, Set<String> stopWords) {
        classifyParagraphs(paragraphs, stopWords, CLASSIFIER_PROPERTIES_DEFAULT);
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     * @param classifierProperties Properties.
     */
    public static void classifyParagraphs(
            List<MutableParagraph> paragraphs, Set<String> stopWords, ClassifierProperties classifierProperties) {

        Set<String> lowerCaseStopWords = new HashSet<>(stopWords.size());

        for (String stopWord : stopWords) {
            lowerCaseStopWords.add(stopWord.toLowerCase());
        }

        for (MutableParagraph paragraph : paragraphs) {
            Classification classification = classify(paragraph, stopWords, classifierProperties);

            paragraph.setClassification(classification);
        }
    }

    /**
     * Performs context sensitive classification. Assumes that {@link #classifyParagraphs(List, Set)} has already been
     * called.
     *
     * @param paragraphs List of paragraphs.
     */
    public static void reviseParagraphs(List<MutableParagraph> paragraphs) {
        reviseParagraphs(paragraphs, CLASSIFIER_PROPERTIES_DEFAULT);
    }

    /**
     * Performs context sensitive classification. Assumes that {@link #classifyParagraphs(List, Set)} has already been
     * called.
     *
     * @param paragraphs List of paragraphs.
     * @param classifierProperties Properties.
     */
    public static void reviseParagraphs(List<MutableParagraph> paragraphs, ClassifierProperties classifierProperties) {
        if (paragraphs.isEmpty()) {
            return;
        }

        ListIterator<MutableParagraph> paragraphIterator = paragraphs.listIterator();

        // Classify short paragraphs

        while (paragraphIterator.hasNext()) {
            MutableParagraph paragraph = paragraphIterator.next();

            if (!SHORT.equals(paragraph.getClassification())) {
                continue;
            }

            int nextIndex = paragraphIterator.nextIndex();

            Classification previousBoundaryClassification = getPrevBoundaryClassification(paragraphs, nextIndex, true);
            Classification nextBoundaryClassification = getNextBoundaryClassification(paragraphs, nextIndex, true);

            Set<Classification> neighbourClassifications = EnumSet.of(
                    previousBoundaryClassification, nextBoundaryClassification);

            Classification newClassification = null;

            if (GOOD_SET.equals(neighbourClassifications)) {
                newClassification = GOOD;
            } else if (BAD_SET.equals(neighbourClassifications)) {
                newClassification = BAD;
            } else {
                if (BAD.equals(previousBoundaryClassification)) {
                    previousBoundaryClassification = getPrevBoundaryClassification(paragraphs, nextIndex, false);
                }

                if (BAD.equals(nextBoundaryClassification)) {
                    nextBoundaryClassification = getNextBoundaryClassification(paragraphs, nextIndex, false);
                }

                if (NEAR_GOOD.equals(previousBoundaryClassification) || NEAR_GOOD.equals(nextBoundaryClassification)) {
                    newClassification = GOOD;
                } else {
                    newClassification = BAD;
                }
            }

            paragraph.setClassification(newClassification);
        }

        // Classify near good paragraphs

        paragraphIterator = paragraphs.listIterator();

        while (paragraphIterator.hasNext()) {
            MutableParagraph paragraph = paragraphIterator.next();

            if (!NEAR_GOOD.equals(paragraph.getClassification())) {
                continue;
            }

            int nextIndex = paragraphIterator.nextIndex();

            Classification previousNeighbourClassification = getPrevBoundaryClassification(
                    paragraphs, nextIndex, true);
            Classification nextNeighbourClassification = getNextBoundaryClassification(
                    paragraphs, nextIndex, true);

            Classification newClassification = null;

            if (BAD.equals(previousNeighbourClassification) && BAD.equals(nextNeighbourClassification)) {
                newClassification = BAD;
            } else {
                newClassification = GOOD;
            }

            paragraph.setClassification(newClassification);
        }
    }

    private static Classification classify(
            Paragraph paragraph, Set<String> stopWords, ClassifierProperties classifierProperties) {

        String domPath = paragraph.getDomPath();
        int length = paragraph.length();
        float linkDensity = paragraph.getLinkDensity();
        float stopWordsDensity = paragraph.getStopWordsDensity(stopWords);
        String text = paragraph.getText();

        Classification classification = null;

        if (linkDensity > classifierProperties.getMaxLinkDensity()) {
            classification = BAD;
        } else if (StringUtils.contains(text, COPYRIGHT_CHAR) || StringUtils.contains(text, COPYRIGHT_CODE)) {
            classification = BAD;
        } else if (StringUtils.isNotEmpty(domPath) && SELECT_PATTERN.matcher(domPath).find()) {
            classification = BAD;
        } else if (length < classifierProperties.getLengthLow()) {
            if (paragraph.getCharsInLinksCount() > 0) {
                classification = BAD;
            } else {
                classification = SHORT;
            }
        } else if (stopWordsDensity >= classifierProperties.getStopwordsHigh()) {
            if (length > classifierProperties.getLengthHigh()) {
                classification = GOOD;
            } else {
                classification = NEAR_GOOD;
            }
        } else if (stopWordsDensity >= classifierProperties.getStopwordsLow()) {
            classification = NEAR_GOOD;
        } else {
            classification = BAD;
        }

        return classification;
    }

    private static Classification getBoundaryClassification(
            List<MutableParagraph> paragraphs, int startIndex, boolean forward, boolean ignoreNearGood) {

        ListIterator<MutableParagraph> paragraphIterator = null;

        try {
            paragraphIterator = paragraphs.listIterator(startIndex);
        } catch (IndexOutOfBoundsException ioobe) {
            return BAD;
        }

        Set<Classification> classificationSet = BAD_GOOD_NEAR_GOOD_SET;

        if (ignoreNearGood) {
            classificationSet = BAD_GOOD_SET;
        }

        while ((forward && paragraphIterator.hasNext()) || (!forward && paragraphIterator.hasPrevious())) {
            MutableParagraph paragraph = null;

            if (forward) {
                paragraph = paragraphIterator.next();
            } else {
                paragraph = paragraphIterator.previous();
            }

            Classification classification = paragraph.getClassification();

            if (classificationSet.contains(classification)) {
                return classification;
            }
        }

        return BAD;
    }

    private static Classification getNextBoundaryClassification(
            List<MutableParagraph> paragraphs, int startIndex, boolean ignoreNearGood) {

        return getBoundaryClassification(paragraphs, startIndex, true, ignoreNearGood);
    }

    private static Classification getPrevBoundaryClassification(
            List<MutableParagraph> paragraphs, int startIndex, boolean ignoreNearGood) {

        return getBoundaryClassification(paragraphs, startIndex, false, ignoreNearGood);
    }

}
