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

import nl.wizenoze.justext.html.HtmlBeautifier;
import nl.wizenoze.justext.paragraph.MutableParagraph;
import nl.wizenoze.justext.paragraph.Paragraph;
import nl.wizenoze.justext.paragraph.ParagraphMaker;
import nl.wizenoze.justext.util.StopWordsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static nl.wizenoze.justext.Classifier.CLASSIFIER_PROPERTIES_DEFAULT;

/**
 * @author László Csontos
 */
public final class JusText {

    private static final boolean IGNORE_BOILERPLATE_DEFAULT = true;
    private static final Set<String> STOP_WORDS_DEFAULT = Collections.emptySet();

    private final ClassifierProperties classifierProperties;
    private final HtmlBeautifier htmlBeautifier;
    private final Set<String> stopWords;

    /**
     * Creates a JusText instance with empty stop words and default classifier properties.
     */
    public JusText() {
        this(CLASSIFIER_PROPERTIES_DEFAULT, STOP_WORDS_DEFAULT);
    }

    /**
     * Creates a JusText instance with empty stop words and the given classifier properties.
     *
     * @param classifierProperties classifier properties.
     */
    public JusText(ClassifierProperties classifierProperties) {
        this(classifierProperties, STOP_WORDS_DEFAULT);
    }

    /**
     * Creates a JusText instance with the given stop words and classifier properties.
     *
     * @param classifierProperties classifier properties.
     * @param stopWords stop-words.
     */
    public JusText(ClassifierProperties classifierProperties, Set<String> stopWords) {
        this.classifierProperties = classifierProperties;
        this.htmlBeautifier = new HtmlBeautifier();
        this.stopWords = stopWords;
    }

    /**
     * Extracts paragraphs from the given HTML.
     *
     * @param html HTML to extract from.
     * @return list of extracted paragraphs.
     */
    public List<Paragraph> extract(String html) {
        String cleanedHtml = htmlBeautifier.cleanHtml(html);
        return doExtract(cleanedHtml, stopWords, IGNORE_BOILERPLATE_DEFAULT);
    }

    /**
     * Extracts paragraphs from the given HTML.
     *
     * @param html HTML to extract from.
     * @param ignoreBoilerplate omit boilerplate paragraphs from the output.
     * @return list of extracted paragraphs.
     */
    public List<Paragraph> extract(String html, boolean ignoreBoilerplate) {
        String cleanedHtml = htmlBeautifier.cleanHtml(html);
        return doExtract(cleanedHtml, stopWords, ignoreBoilerplate);
    }

    /**
     * Extracts paragraphs from the given HTML.
     *
     * @param html HTML to extract from.
     * @param languageCode use stop words determined by this language code.
     * @return list of extracted paragraphs.
     */
    public List<Paragraph> extract(String html, String languageCode) {
        return extract(html, languageCode, IGNORE_BOILERPLATE_DEFAULT);
    }

    /**
     * Extracts paragraphs from the given HTML.
     *
     * @param html HTML to extract from.
     * @param languageCode use stop words determined by this language code.
     * @param ignoreBoilerplate omit boilerplate paragraphs from the output.
     * @return list of extracted paragraphs.
     */
    public List<Paragraph> extract(String html, String languageCode, boolean ignoreBoilerplate) {
        Set<String> stopWordsLocal = STOP_WORDS_DEFAULT;

        if (languageCode != null) {
            stopWordsLocal = StopWordsUtil.getStopWords(languageCode);
        }

        return extract(html, stopWordsLocal, ignoreBoilerplate);
    }

    /**
     * Extracts paragraphs from the given HTML.
     *
     * @param html HTML to extract from.
     * @param stopWords use these stop words.
     * @return list of extracted paragraphs.
     */
    public List<Paragraph> extract(String html, Set<String> stopWords) {
        String cleanedHtml = htmlBeautifier.cleanHtml(html);
        return doExtract(cleanedHtml, stopWords, IGNORE_BOILERPLATE_DEFAULT);
    }

    /**
     * Extracts paragraphs from the given HTML.
     *
     * @param html HTML to extract from.
     * @param stopWords use these stop words.
     * @param ignoreBoilerplate omit boilerplate paragraphs from the output.
     * @return list of extracted paragraphs.
     */
    public List<Paragraph> extract(String html, Set<String> stopWords, boolean ignoreBoilerplate) {
        String cleanedHtml = htmlBeautifier.cleanHtml(html);
        return doExtract(cleanedHtml, stopWords, ignoreBoilerplate);
    }

    private List<Paragraph> doExtract(String cleanedHtml, Set<String> stopWords, boolean ignoreBoilerplate) {
        // Parse cleaned HTML

        ParagraphMaker paragraphMaker = new ParagraphMaker(cleanedHtml);
        List<MutableParagraph> paragraphs = paragraphMaker.traverse();

        // Context-free classification

        Classifier.classifyParagraphs(paragraphs, stopWords, classifierProperties);

        // Context-sensitive classification

        Classifier.reviseParagraphs(paragraphs, classifierProperties);

        // Freeze paragraphs

        List<Paragraph> frozenParagraphs = new ArrayList<>(paragraphs.size());

        paragraphs.forEach((MutableParagraph paragraph) -> {
            if (!paragraph.isBoilerplate() || !ignoreBoilerplate) {
                frozenParagraphs.add(paragraph.freeze());
            }
        });

        return frozenParagraphs;
    }

}
