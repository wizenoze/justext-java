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

import java.util.Set;

/**
 * Created by lcsontos on 1/8/16.
 */
public final class Paragraph {

    private int charsInLinksCount = 0;
    private Classification classification;
    private String domPath;
    private boolean isBoilerplace = false;
    private boolean isHeading = false;
    private int tagsCount = 0;
    private String text;
    private String[] textNodes;
    private String[] words;
    private String xpath;

    /**
     * Creates an empty paragraph.
     */
    public Paragraph() {
    }

    /**
     * Creates a paragraph with the given text nodes.
     * @param textNodes text nodes.
     */
    public Paragraph(String[] textNodes) {
        this.textNodes = textNodes;
    }

    /**
     * Creates a paragraph with the given text nodes and character count in links.
     * @param textNodes text nodes.
     * @param charsInLinksCount count of characters in links.
     */
    public Paragraph(String[] textNodes, int charsInLinksCount) {
        this.textNodes = textNodes;
        this.charsInLinksCount = charsInLinksCount;
    }

    /**
     * Gets count of characters in links.
     * @return count of characters in links.
     */
    public int getCharsInLinksCount() {
        return charsInLinksCount;
    }

    /**
     * Gets classification.
     * @return classification.
     */
    public Classification getClassification() {
        return classification;
    }

    /**
     * Gets DOM path.
     * @return DOM path.
     */
    public String getDomPath() {
        return domPath;
    }

    /**
     * Gets link density.
     * @return link density.
     */
    public float getLinkDensity() {
        int textLength = length();

        if (textLength == 0) {
            return 0;
        }

        return 1.0f * charsInLinksCount / textLength;
    }

    /**
     * Gets stop words count.
     * @param stopWords words.
     * @return stop words count.
     */
    public int getStopWordsCount(Set<String> stopWords) {
        int stopWordsCount = 0;

        for (String word : words()) {
            if (stopWords.contains(word.toLowerCase())) {
                stopWordsCount++;
            }
        }

        return stopWordsCount;
    }

    /**
     * Gets stop words density.
     * @param stopWords words.
     * @return stop words density.
     */
    public float getStopWordsDensity(Set<String> stopWords) {
        int wordsCount = getWordsCount();

        if (wordsCount == 0) {
            return 0;
        }

        float stopWordsDensity = 1.0f * getStopWordsCount(stopWords) / wordsCount;

        return stopWordsDensity;
    }

    /**
     * Gets tags count.
     * @return tags count.
     */
    public int getTagsCount() {
        return tagsCount;
    }

    /**
     * Concatenates text nodes into a single text.
     * @return Concatenated text of nodes.
     */
    public String getText() {
        if (text == null) {
            text = StringUtils.join(textNodes, StringUtils.SPACE);
        }

        return text;
    }

    /**
     * Gets text nodes.
     * @return text nodes.
     */
    public String[] getTextNodes() {
        return textNodes;
    }

    /**
     * Gets word count.
     * @return word count.
     */
    public int getWordsCount() {
        return words().length;
    }

    /**
     * Gets XPath.
     * @return xpath.
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * Returns if this paragraph is boilerplate.
     * @return true if boilerplate, false otherwise.
     */
    public boolean isBoilerplace() {
        return isBoilerplace;
    }

    /**
     * Returns if this paragraph is heading.
     * @return true if heading, false otherwise.
     */
    public boolean isHeading() {
        return isHeading;
    }

    /**
     * Returns the full length of this paragraph.
     * @return length of this paragraph.
     */
    public int length() {
        return getText().length();
    }

    /**
     * Sets count of characters in links.
     * @param charsInLinksCount count of characters in links.
     */
    public void setCharsInLinksCount(int charsInLinksCount) {
        this.charsInLinksCount = charsInLinksCount;
    }

    /**
     * Sets classification.
     * @param classification classification.
     */
    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    /**
     * Sets DOM path.
     * @param domPath DOM path.
     */
    public void setDomPath(String domPath) {
        this.domPath = domPath;
    }

    /**
     * Sets if this paragraph is boilerplate.
     * @param isBoilerplace true if boilerplate, false otherwise.
     */
    public void setIsBoilerplace(boolean isBoilerplace) {
        this.isBoilerplace = isBoilerplace;
    }

    /**
     * Sets if this paragraph is heading.
     * @param isHeading true if heading, false otherwise.
     */
    public void setIsHeading(boolean isHeading) {
        this.isHeading = isHeading;
    }

    /**
     * Sets tags count.
     * @param tagsCount tags count.
     */
    public void setTagsCount(int tagsCount) {
        this.tagsCount = tagsCount;
    }

    /**
     * Sets text nodes.
     * @param textNodes text nodes.
     */
    public void setTextNodes(String[] textNodes) {
        text = null;
        words = null;

        this.textNodes = textNodes;
    }

    /**
     * Sets XPath.
     * @param xpath xpath.
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    private String[] words() {
        if (words == null) {
            words = StringUtils.split(getText());
        }

        return words;
    }

}
