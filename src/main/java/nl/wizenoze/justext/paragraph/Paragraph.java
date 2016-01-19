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

package nl.wizenoze.justext.paragraph;

import java.util.List;
import java.util.Set;

import nl.wizenoze.justext.Classification;

/**
 * Created by lcsontos on 1/14/16.
 */
public interface Paragraph {

    /**
     * Gets count of characters in links.
     * @return count of characters in links.
     */
    int getCharsInLinksCount();

    /**
     * Gets that classification with which {@link MutableParagraph#setClassification(Classification)} was called for the
     * last time.
     *
     * @return classification.
     */
    Classification getClassification();

    /**
     * Gets that classification with which {@link MutableParagraph#setClassification(Classification)} was called for the
     * first time.
     *
     * @return classification.
     */
    Classification getFirstClassification();

    /**
     * Gets DOM path.
     * @return DOM path.
     */
    String getDomPath();

    /**
     * Gets link density.
     * @return link density.
     */
    float getLinkDensity();

    /**
     * Gets stop words count.
     * @param stopWords words.
     * @return stop words count.
     */
    int getStopWordsCount(Set<String> stopWords);

    /**
     * Gets stop words density.
     * @param stopWords words.
     * @return stop words density.
     */
    float getStopWordsDensity(Set<String> stopWords);

    /**
     * Gets tags count.
     * @return tags count.
     */
    int getTagsCount();

    /**
     * Concatenates text nodes into a single text.
     * @return Concatenated text of nodes.
     */
    String getText();

    /**
     * Gets words.
     * @return words
     */
    List<String> getWords();

    /**
     * Gets word count.
     * @return word count.
     */
    int getWordsCount();

    /**
     * Gets XPath.
     * @return xpath.
     */
    String getXpath();

    /**
     * Returns if this paragraph contains text.
     *
     * @return true if this paragraph contains text.
     */
    boolean hasText();

    /**
     * Returns if this paragraph is boilerplate.
     * @return true if boilerplate, false otherwise.
     */
    boolean isBoilerplate();

    /**
     * Returns if this paragraph is heading.
     * @return true if heading, false otherwise.
     */
    boolean isHeading();

    /**
     * Returns if this paragraph is within a SELECT element.
     * @return true if it's within a SELECT, false otherwise.
     */
    boolean isSelect();

    /**
     * Returns the full length of this paragraph.
     * @return length of this paragraph.
     */
    int length();

}
