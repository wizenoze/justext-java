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

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import nl.wizenoze.justext.exception.JusTextParseException;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import static nl.wizenoze.justext.util.StringPool.A;
import static nl.wizenoze.justext.util.StringPool.BR;

/**
 * Created by lcsontos on 1/11/16.
 */
public class ParagraphMaker {

    private static final Logger LOG = LoggerFactory.getLogger(ParagraphMaker.class);

    private static final Set<String> TEXTUAL_TAGS;

    private final List<MutableParagraph> paragraphs;
    private final PathInfo pathInfo;
    private final XMLStreamReader streamReader;

    private boolean isLink = false;
    private boolean isBreak = false;
    private MutableParagraph lastParagraph;

    static {
        Stream<String> textualTagsStream = Arrays.stream(TextualTag.values()).map(Enum::name);
        TEXTUAL_TAGS = textualTagsStream.collect(Collectors.toSet());
    }

    /**
     * Create a paragraph maker.
     * @param reader XML document reader.
     * @throws Exception upon error.
     */
    public ParagraphMaker(Reader reader) {
        paragraphs = new ArrayList<>();
        pathInfo = new PathInfo();

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();

        try {
            streamReader = inputFactory.createXMLStreamReader(new StreamSource(reader));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JusTextParseException(e.getMessage(), e);
        }
    }

    /**
     * Create a paragraph maker.
     * @param xml XML document.
     * @throws Exception upon error.
     */
    public ParagraphMaker(String xml) {
        this(new StringReader(xml));
    }

    /**
     * Traverses the document hierarchy.
     * @return list of paragraphs.
     * @throws Exception upon error.
     */
    public final List<MutableParagraph> traverse() {
        try {
            return doTraverse();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JusTextParseException(e.getMessage(), e);
        }
    }

    private List<MutableParagraph> doTraverse() throws XMLStreamException {
        if (!streamReader.hasNext()) {
            return Collections.unmodifiableList(paragraphs);
        }

        while (streamReader.hasNext()) {
            int event = streamReader.next();

            String tagName = null;

            switch (event) {
                case START_ELEMENT:
                    tagName = streamReader.getLocalName().toUpperCase();

                    pathInfo.append(tagName);

                    boolean tagNameIsBR = BR.equals(tagName);

                    if (TEXTUAL_TAGS.contains(tagName) || (tagNameIsBR && isBreak)) {
                        if (tagNameIsBR) {
                            lastParagraph.decrementTagsCount();
                        }

                        startNewParagraph();
                    } else {
                        isBreak = tagNameIsBR;

                        if (A.equals(tagName)) {
                            isLink = true;
                        }

                        if (lastParagraph != null) {
                            lastParagraph.incrementTagsCount();
                        }
                    }

                    break;
                case END_DOCUMENT:
                    startNewParagraph();

                    break;
                case END_ELEMENT:
                    tagName = streamReader.getLocalName().toUpperCase();

                    pathInfo.pop();

                    if (TEXTUAL_TAGS.contains(tagName)) {
                        startNewParagraph();
                    }

                    if (A.equals(tagName)) {
                        isLink = false;
                    }

                    break;
                case CHARACTERS:
                    String text = streamReader.getText();

                    if (StringUtils.isNotBlank(text)) {
                        text = lastParagraph.appendText(text);

                        if (isLink) {
                            lastParagraph.incrementCharsInLinksCount(text.length());
                        }

                        isBreak = false;
                    }

                    break;
                default:
                    // Ignore other XML stream events.
            }
        }

        return Collections.unmodifiableList(paragraphs);
    }

    private void startNewParagraph() {
        if ((lastParagraph != null) && lastParagraph.hasText()) {
            paragraphs.add(lastParagraph);
        }

        lastParagraph = new MutableParagraphImpl(pathInfo);
    }

}
