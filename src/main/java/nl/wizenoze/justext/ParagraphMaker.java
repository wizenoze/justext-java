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
import java.util.Deque;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by lcsontos on 1/11/16.
 */
public class ParagraphMaker {

    private final XMLStreamReader streamReader;
    private Deque<String> path;

    /**
     * Create a paragraph maker.
     * @param document document.
     * @throws Exception upon error.
     */
    public ParagraphMaker(Document document) throws Exception {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        streamReader = inputFactory.createXMLStreamReader(new DOMSource(document));
    }

    /**
     * Traverses the document hierarchy.
     * @param document document.
     * @return list of paragraphs.
     * @throws Exception upon error.
     */
    public final List<Paragraph> traverse(Document document) throws Exception {
        List<Paragraph> paragraphs = new ArrayList<>();

        while (streamReader.hasNext()) {
            int event = streamReader.next();

            switch (event) {
                case START_ELEMENT:
                case END_ELEMENT:
                case CHARACTERS:
                default:
            }
        }

        return paragraphs;
    }

}
