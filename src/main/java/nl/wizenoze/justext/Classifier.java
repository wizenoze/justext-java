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

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the implementation of the following context free classification algorithm.
 *
 * @author László Csontos
 * @see http://corpus.tools/wiki/Justext/Algorithm
 */
public final class Classifier {

    private static final char COPYRIGHT_CHAR = '\u00a9';
    private static final String COPYRIGHT_CODE = "&copy;";

    private Classifier() {
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     */
    public static void classifyParagraphs(List<Paragraph> paragraphs, Set<String> stopWords) {
        classifyParagraphs(paragraphs, stopWords, ClassifierProperties.getDefault());
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     * @param classifierProperties Properties.
     */
    public static void classifyParagraphs(
            List<Paragraph> paragraphs, Set<String> stopWords, ClassifierProperties classifierProperties) {

        Set<String> lowerCaseStopWords = new HashSet<>(stopWords.size());

        for (String stopWord : stopWords) {
            lowerCaseStopWords.add(stopWord.toLowerCase());
        }

        for (Paragraph paragraph : paragraphs) {
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

            paragraph.setClassification(classification);
        }
    }

}
