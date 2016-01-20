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
 * along with justext-java.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.wizenoze.justext;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import nl.wizenoze.justext.paragraph.MutableParagraph;
import nl.wizenoze.justext.paragraph.Paragraph;
import nl.wizenoze.justext.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @see <a href="http://corpus.tools/wiki/Justext/Algorithm">Justext algorithm</a>
 */
public final class Classifier {

    /**
     * Default classifier properties.
     */
    public static final ClassifierProperties CLASSIFIER_PROPERTIES_DEFAULT = ClassifierProperties.getDefault();

    private static final Logger LOG = LoggerFactory.getLogger(Classifier.class);

    private static final Set<Classification> BAD_SET = EnumSet.of(BAD);
    private static final Set<Classification> BAD_GOOD_SET = EnumSet.of(BAD, GOOD);
    private static final Set<Classification> BAD_GOOD_NEAR_GOOD_SET = EnumSet.of(BAD, GOOD, NEAR_GOOD);
    private static final Set<Classification> GOOD_SET = EnumSet.of(GOOD);

    /*
     * Classification sets can never have more than two elements (GOOD/BAD, BAD/NEAR_GOOD, GOOD/NEAR_GOOD).
     */
    private static final int CLASSIFICATION_SET_MAX_SIZE = 2;

    private Classifier() {
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     */
    public static void classifyContextFree(List<MutableParagraph> paragraphs, Set<String> stopWords) {
        classifyContextFree(paragraphs, stopWords, CLASSIFIER_PROPERTIES_DEFAULT);
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of lower-case stop words.
     * @param classifierProperties Properties.
     */
    public static void classifyContextFree(
            List<MutableParagraph> paragraphs, Set<String> stopWords, ClassifierProperties classifierProperties) {

        for (MutableParagraph paragraph : paragraphs) {
            Classification classification = doClassifyContextFree(paragraph, stopWords, classifierProperties);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Classified context-free {} as {}", paragraph.getText(), classification);
            }

            paragraph.setClassification(classification);
        }
    }

    /**
     * Performs context sensitive classification. Assumes that {@link #classifyContextFree(List, Set)} has already been
     * called.
     *
     * @param paragraphs List of paragraphs.
     */
    public static void classifyContextSensitive(List<MutableParagraph> paragraphs) {
        classifyContextSensitive(paragraphs, CLASSIFIER_PROPERTIES_DEFAULT);
    }

    /**
     * Performs context sensitive classification. Assumes that {@link #classifyContextFree(List, Set)} has already been
     * called.
     *
     * @param paragraphs List of paragraphs.
     * @param classifierProperties Properties.
     */
    public static void classifyContextSensitive(
            List<MutableParagraph> paragraphs, ClassifierProperties classifierProperties) {

        if (paragraphs.isEmpty()) {
            return;
        }

        ListIterator<MutableParagraph> paragraphIterator = paragraphs.listIterator();

        // Classify short paragraphs

        while (paragraphIterator.hasNext()) {
            MutableParagraph paragraph = paragraphIterator.next();

            Classification classification = paragraph.getClassification();

            if (!SHORT.equals(classification)) {
                continue;
            }

            int nextIndex = paragraphIterator.nextIndex();

            MergedBoundaryClassifications mergedBoundaryClassifications = mergeBoundaryClassifications(
                    paragraph, paragraphs, nextIndex);
            Set<Classification> neighbourClassifications = mergedBoundaryClassifications.getMergedClassifications();

            Classification newClassification = null;

            // For BAD/BAD and GOOD/GOOD, it's an easy decision.
            if (GOOD_SET.equals(neighbourClassifications)) {
                newClassification = GOOD;
            } else if (BAD_SET.equals(neighbourClassifications)) {
                newClassification = BAD;
            } else {
                // Classification set must be BAD/GOOD, take NEAR_GOOD into account in this case.
                if (mergedBoundaryClassifications.isNearGoodRemoved()) {
                    newClassification = GOOD;
                } else {
                    newClassification = BAD;
                }
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(
                        "Classified context-sensitive {} --> from {} to {}",
                        paragraph.getText(), classification, newClassification);
            }

            paragraph.setClassification(newClassification);
        }

        // Classify near good paragraphs

        paragraphIterator = paragraphs.listIterator();

        while (paragraphIterator.hasNext()) {
            MutableParagraph paragraph = paragraphIterator.next();

            Classification classification = paragraph.getClassification();

            if (!NEAR_GOOD.equals(classification)) {
                continue;
            }

            int nextIndex = paragraphIterator.nextIndex();

            MergedBoundaryClassifications mergedBoundaryClassifications = mergeBoundaryClassifications(
                    paragraph, paragraphs, nextIndex);
            Set<Classification> neighbourClassifications = mergedBoundaryClassifications.getMergedClassifications();

            Classification newClassification = null;

            if (BAD_SET.equals(neighbourClassifications)) {
                newClassification = BAD;
            } else {
                newClassification = GOOD;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(
                        "Classified context-sensitive {} --> from {} to {}",
                        paragraph.getText(), classification, newClassification);
            }

            paragraph.setClassification(newClassification);
        }
    }

    private static <P extends Paragraph> ListIterator<P> createListIterator(List<P> paragraphs, int startIndex) {
        try {
            return paragraphs.listIterator(startIndex);
        } catch (IndexOutOfBoundsException ioobe) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(ioobe.getMessage());
            }

            return null;
        }
    }

    private static Classification doClassifyContextFree(
            Paragraph paragraph, Set<String> stopWords, ClassifierProperties classifierProperties) {

        if (paragraph.getLinkDensity() > classifierProperties.getMaxLinkDensity()) {
            return BAD;
        }

        String text = paragraph.getText();

        if (StringUtil.contains(text, COPYRIGHT_CHAR) || StringUtil.contains(text, COPYRIGHT_CODE)) {
            return BAD;
        }

        if (paragraph.isSelect()) {
            return BAD;
        }

        int length = paragraph.length();

        if (length < classifierProperties.getLengthLow()) {
            if (paragraph.getCharsInLinksCount() > 0) {
                return BAD;
            }

            return SHORT;
        }

        float stopWordsDensity = paragraph.getStopWordsDensity(stopWords);

        if (stopWordsDensity >= classifierProperties.getStopWordsHigh()) {
            if (length > classifierProperties.getLengthHigh()) {
                return GOOD;
            }

            return NEAR_GOOD;
        }

        if (stopWordsDensity >= classifierProperties.getStopWordsLow()) {
            return NEAR_GOOD;
        }

        return BAD;
    }

    private static Set<Classification> findBoundaryClassifications(
            ListIterator<MutableParagraph> paragraphIterator, boolean forward) {

        if (paragraphIterator == null) {
            return BAD_SET;
        }

        Set<Classification> classificationSet = new HashSet<>(CLASSIFICATION_SET_MAX_SIZE);

        while ((forward && paragraphIterator.hasNext()) || (!forward && paragraphIterator.hasPrevious())) {
            MutableParagraph paragraph = null;

            if (forward) {
                paragraph = paragraphIterator.next();
            } else {
                paragraph = paragraphIterator.previous();
            }

            Classification classification = paragraph.getClassification();

            if (!BAD_GOOD_NEAR_GOOD_SET.contains(classification)) {
                continue;
            }

            classificationSet.add(classification);

            if (BAD_GOOD_SET.contains(classification)) {
                return classificationSet;
            }
        }

        return BAD_SET;
    }

    private static MergedBoundaryClassifications mergeBoundaryClassifications(
            MutableParagraph paragraph, List<MutableParagraph> paragraphs, int startIndex) {

        // Find boundary classifications
        Set<Classification> nextClassifications = findBoundaryClassifications(
                createListIterator(paragraphs, startIndex), true);

        Set<Classification> prevClassifications = findBoundaryClassifications(
                createListIterator(paragraphs, startIndex), false);

        // Remove NEAR_GOOD from the final output either way.
        boolean nearGoodRemovedFromNext = nextClassifications.remove(NEAR_GOOD);
        boolean nearGoodRemovedFromPrev = prevClassifications.remove(NEAR_GOOD);

        // For good classifications we don't need NEAR_GOOD.
        if (GOOD_SET.equals(nextClassifications)) {
            nearGoodRemovedFromNext = false;
        }

        if (GOOD_SET.equals(prevClassifications)) {
            nearGoodRemovedFromPrev = false;
        }

        // Merge classifications.
        Set<Classification> mergedClassifications = new HashSet<>(CLASSIFICATION_SET_MAX_SIZE);
        mergedClassifications.addAll(nextClassifications);
        mergedClassifications.addAll(prevClassifications);

        boolean nearGoodRemoved = (nearGoodRemovedFromNext || nearGoodRemovedFromPrev);

        // If the resulting classification set isn't a BAD/GOOD combination, we don't take NEAR_GOOD classifications
        // into account.
        if (!BAD_GOOD_SET.equals(mergedClassifications)) {
            nearGoodRemoved = false;
        }

        return new MergedBoundaryClassifications(paragraph, mergedClassifications, nearGoodRemoved);
    }

    /*
     * Internal class representing the merged neighbour classifications of a paragraph.
     */
    private static class MergedBoundaryClassifications {

        private final Set<Classification> mergedClassifications;
        private final boolean nearGoodRemoved;
        private final MutableParagraph paragraph;

        MergedBoundaryClassifications(
                MutableParagraph paragraph, Set<Classification> mergedClassifications, boolean nearGoodRemoved) {

            this.paragraph = paragraph;
            this.mergedClassifications = mergedClassifications;
            this.nearGoodRemoved = nearGoodRemoved;
        }

        public Set<Classification> getMergedClassifications() {
            return mergedClassifications;
        }

        public boolean isNearGoodRemoved() {
            return nearGoodRemoved;
        }

        public MutableParagraph getParagraph() {
            return paragraph;
        }

    }

}
