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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nl.wizenoze.justext.exception.JusTextParseException;
import nl.wizenoze.justext.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static nl.wizenoze.justext.util.StringPool.ATTRIBUTE_ALT;
import static nl.wizenoze.justext.util.StringPool.ATTRIBUTE_SRC;
import static nl.wizenoze.justext.util.StringPool.TAG_A;
import static nl.wizenoze.justext.util.StringPool.TAG_BR;
import static nl.wizenoze.justext.util.StringPool.TAG_IMG;

/**
 * Traverses the given XML document and constructs a list of {@link MutableParagraph} objects.
 *
 * @author László Csontos
 */
public class ParagraphMaker {

    private static final Logger LOG = LoggerFactory.getLogger(ParagraphMaker.class);

    private static final Set<String> TEXTUAL_TAGS;

    private final DefaultHandler paragraphHandler;
    private final List<MutableParagraph> paragraphs;
    private final SAXParser parser;
    private final PathInfo pathInfo;
    private final InputSource source;

    private boolean isImage = false;
    private boolean isLink = false;
    private boolean isBreak = false;
    private MutableParagraph lastParagraph;

    static {
        Stream<String> textualTagsStream = Arrays.stream(
                ParagraphTag.values()).map(Enum::name).map(String::toLowerCase);
        TEXTUAL_TAGS = textualTagsStream.collect(Collectors.toSet());
    }

    /**
     * Create a paragraph maker.
     * @param reader XML document reader.
     */
    public ParagraphMaker(Reader reader) {
        this(new InputSource(reader));
    }

    /**
     * Create a paragraph maker.
     * @param xml XML document.
     */
    public ParagraphMaker(String xml) {
        this(new InputSource(new StringReader(xml)));
    }

    ParagraphMaker(InputSource source) {
        this.source = source;

        paragraphHandler = new ParagraphHandler();
        paragraphs = new ArrayList<>();
        pathInfo = new PathInfo();

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();

        try {
            parser = parserFactory.newSAXParser();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JusTextParseException(e.getMessage(), e);
        }
    }

    /**
     * Traverses the document hierarchy.
     * @return list of paragraphs.
     */
    public final List<MutableParagraph> traverse() {
        try {
            return doTraverse();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JusTextParseException(e.getMessage(), e);
        }
    }

    private List<MutableParagraph> doTraverse() throws IOException, SAXException {
        parser.parse(source, paragraphHandler);

        return Collections.unmodifiableList(paragraphs);
    }

    private void startNewParagraph() {
        if ((lastParagraph != null) && (lastParagraph.hasText() || lastParagraph.isImage())) {
            paragraphs.add(lastParagraph);
        }

        lastParagraph = new MutableParagraphImpl(pathInfo);
    }

    private class ParagraphHandler extends DefaultHandler {

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String text = String.valueOf(ch, start, length);

            if (isImage) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Ignoring text \"{}\" within an IMG tag.", StringUtil.shorten(text));
                }

                return;
            }

            if (StringUtil.isNotBlank(text)) {
                text = lastParagraph.appendText(text);

                if (isLink) {
                    lastParagraph.incrementCharsInLinksCount(text.length());
                }

                isBreak = false;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            startNewParagraph();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String tagName = getName(localName, qName);

            pathInfo.pop();

            if (TEXTUAL_TAGS.contains(tagName)) {
                startNewParagraph();
            }

            if (TAG_A.equals(tagName)) {
                isLink = false;
            }

            if (TAG_IMG.equals(tagName)) {
                isImage = false;
            }
        }

        @Override
        public void startElement(
                String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

            String tagName = getName(localName, qName);

            pathInfo.append(tagName);

            boolean tagNameIsBR = TAG_BR.equals(tagName);

            if (TEXTUAL_TAGS.contains(tagName) || (tagNameIsBR && isBreak)) {
                if (tagNameIsBR) {
                    lastParagraph.decrementTagsCount();
                }

                startNewParagraph();

                if (TAG_IMG.equals(tagName)) {
                    isImage = true;

                    for (int index = 0; index < attributes.getLength(); index++) {
                        String attributeName = getName(attributes.getLocalName(index), attributes.getQName(index));
                        String attributeValue = attributes.getValue(index);

                        if (StringUtil.isBlank(attributeValue)) {
                            continue;
                        }

                        switch (attributeName) {
                            case ATTRIBUTE_ALT:
                                lastParagraph.appendText(attributeValue);
                                break;
                            case ATTRIBUTE_SRC:
                                lastParagraph.setUrl(attributeValue);
                                break;
                            default:
                                // Ignore other attributes
                        }
                    }
                }
            } else {
                isBreak = tagNameIsBR;

                if (TAG_A.equals(tagName)) {
                    isLink = true;
                }

                if (lastParagraph != null) {
                    lastParagraph.incrementTagsCount();
                }
            }
        }

        private String getName(String localName, String qName) {
            String tagName = qName;

            if (StringUtil.isNotEmpty(localName)) {
                tagName = localName;
            }

            return tagName.toLowerCase();
        }

    }

}
