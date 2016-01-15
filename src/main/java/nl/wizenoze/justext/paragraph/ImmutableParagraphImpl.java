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

package nl.wizenoze.justext.paragraph;

import nl.wizenoze.justext.Classification;

import java.util.List;
import java.util.Set;

/**
 * Created by lcsontos on 1/14/16.
 */
public final class ImmutableParagraphImpl extends BaseParagraph {

    private final int charsInLinksCount;
    private final Classification classification;
    private final String domPath;
    private final boolean hasText;
    private final boolean isBoilerplace;
    private final boolean isHeading;
    private final boolean isSelect;
    private final int length;
    private final float linkDensity;
    private final int tagsCount;
    private final String text;
    private final List<String> words;
    private final int wordsCount;
    private final String xpath;

    /**
     * Creates an immutable copy of the given paragraph.
     * @param paragraph paragraph to make immutable
     */
    public ImmutableParagraphImpl(Paragraph paragraph) {
        charsInLinksCount = paragraph.getCharsInLinksCount();
        classification = paragraph.getClassification();
        domPath = paragraph.getDomPath();
        isBoilerplace = paragraph.isBoilerplace();
        isHeading = paragraph.isHeading();
        isSelect = paragraph.isSelect();
        linkDensity = paragraph.getLinkDensity();
        tagsCount = paragraph.getTagsCount();
        xpath = paragraph.getXpath();

        text = paragraph.getText();
        hasText = hasText(text);
        length = text.length();

        words = paragraph.getWords();
        wordsCount = words.size();
    }

    @Override
    public int getCharsInLinksCount() {
        return charsInLinksCount;
    }

    @Override
    public Classification getClassification() {
        return classification;
    }

    @Override
    public String getDomPath() {
        return domPath;
    }

    @Override
    public float getLinkDensity() {
        return linkDensity;
    }

    @Override
    public int getStopWordsCount(Set<String> stopWords) {
        String[] wordArray = new String[getWordsCount()];
        return getStopWordsCount(getWords().toArray(wordArray), stopWords);
    }

    @Override
    public float getStopWordsDensity(Set<String> stopWords) {
        String[] wordArray = new String[getWordsCount()];
        return getStopWordsDensity(getWords().toArray(wordArray), stopWords);
    }

    @Override
    public int getTagsCount() {
        return tagsCount;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<String> getWords() {
        return words;
    }

    @Override
    public int getWordsCount() {
        return wordsCount;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    @Override
    public boolean hasText() {
        return hasText;
    }

    @Override
    public boolean isBoilerplace() {
        return isBoilerplace;
    }

    @Override
    public boolean isHeading() {
        return isHeading;
    }

    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public int length() {
        return length;
    }

}
