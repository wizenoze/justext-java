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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import nl.wizenoze.justext.paragraph.MutableParagraph;
import nl.wizenoze.justext.paragraph.Paragraph;

import org.apache.commons.lang3.StringUtils;

/**
 * This is the implementation of the following context free classification algorithm.
 *
 * @author László Csontos
 * @see http://corpus.tools/wiki/Justext/Algorithm
 */
public final class Classifier {

    private static final Set<Classification> BAD_GOOD = EnumSet.of(Classification.BAD, Classification.GOOD);
    private static final Set<Classification> BAD_GOOD_NEAR_GOOD = EnumSet.of(Classification.BAD, Classification.GOOD, Classification.NEAR_GOOD);
    private static final char COPYRIGHT_CHAR = '\u00a9';
    private static final String COPYRIGHT_CODE = "&copy;";
    private static final ClassifierProperties CLASSIFIER_PROPERTIES_DEFAULT = ClassifierProperties.getDefault();

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

        // Copy old classifications

        List<Classification> oldClassifications = new ArrayList<>(paragraphs.size());
        paragraphs.forEach((Paragraph paragraph) -> oldClassifications.add(paragraph.getClassification()));

        // Classify short paragraphs
    }

    private static Classification getNeighbourClassification(
            ListIterator<MutableParagraph> paragraphIterator, boolean forward, boolean ignoreNearGood) {

        while ((forward && paragraphIterator.hasNext()) || (!forward && paragraphIterator.hasPrevious())) {
            MutableParagraph paragraph = null;

            if (forward) {
                paragraph = paragraphIterator.next();
            } else {
                paragraph = paragraphIterator.previous();
            }

            Classification classification = paragraph.getClassification();

            if (BAD_GOOD.contains(classification)) {
                return classification;
            }
        }

        return Classification.BAD;
    }

    private static Classification classify(
            Paragraph paragraph, Set<String> stopWords, ClassifierProperties classifierProperties) {

        int length = paragraph.length();
        float linkDensity = paragraph.getLinkDensity();
        float stopWordsDensity = paragraph.getStopWordsDensity(stopWords);
        String text = paragraph.getText();

        Classification classification = null;

        if (linkDensity > classifierProperties.getMaxLinkDensity()) {
            classification = Classification.BAD;
        } else if (StringUtils.contains(text, COPYRIGHT_CHAR) || StringUtils.contains(text, COPYRIGHT_CODE)) {
            classification = Classification.BAD;
            // TODO add regex search for SELECT elements here
        } else if (false/*regsearch*/) {
            classification = Classification.BAD;
        } else if (length < classifierProperties.getLengthLow()) {
            if (paragraph.getCharsInLinksCount() > 0) {
                classification = Classification.BAD;
            } else {
                classification = Classification.SHORT;
            }
        } else if (stopWordsDensity >= classifierProperties.getStopwordsHigh()) {
            if (length > classifierProperties.getLengthHigh()) {
                classification = Classification.GOOD;
            } else {
                classification = Classification.NEAR_GOOD;
            }
        } else if (stopWordsDensity >= classifierProperties.getStopwordsLow()) {
            classification = Classification.NEAR_GOOD;
        } else {
            classification = Classification.BAD;
        }

        return classification;
    }

}
