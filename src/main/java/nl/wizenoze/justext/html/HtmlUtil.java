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

package nl.wizenoze.justext.html;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.htmlcleaner.BrowserCompactXmlSerializer;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.Serializer;
import org.htmlcleaner.TagNode;

import org.w3c.dom.Document;

/**
 * Created by lcsontos on 1/7/16.
 */
public final class HtmlUtil {

    private static final Log LOG = LogFactory.getLog(HtmlUtil.class);

    private static final CleanerProperties CLEANER_PROPERTIES;
    private static final HtmlCleaner HTML_CLEANER;
    private static final Serializer SERIALIZER;

    static {
        CLEANER_PROPERTIES = createCleanerProperties();
        HTML_CLEANER = new HtmlCleaner(CLEANER_PROPERTIES);
        SERIALIZER = new BrowserCompactXmlSerializer(CLEANER_PROPERTIES);
    }

    /**
     *
     */
    private HtmlUtil() {
    }

    /**
     * Cleans the given HTML document.
     *
     * @param html HTML document to clean.
     * @return Root node of the cleaned HTML tree.
     */
    public static TagNode clean(String html) {
        return HTML_CLEANER.clean(html);

    }

    /**
     * Cleans the given HTML document.
     *
     * @param html HTML document to clean.
     * @return Cleaned HTML document as a DOM {@link Document}.
     */
    public static Document cleanDom(String html) {
        TagNode tagNode = clean(html);

        DomSerializer domSerializer = new DomSerializer(CLEANER_PROPERTIES);

        try {
            return domSerializer.createDOM(tagNode);
        } catch (ParserConfigurationException pce) {
            LOG.error(pce.getMessage(), pce);
            return null;
        }
    }

    /**
     * Cleans the given HTML document.
     *
     * @param html HTML document to clean.
     * @return Cleaned HTML document as a string.
     */
    public static String cleanHtml(String html) {
        TagNode tagNode = clean(html);

        StringWriter tagNodeWriter = new StringWriter();

        try {
            SERIALIZER.write(tagNode, tagNodeWriter, "utf-8");
        } catch (IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
            return null;
        }

        String serializedHtml = tagNodeWriter.toString();

        serializedHtml = StringUtils.remove(serializedHtml, '\n');
        serializedHtml = StringUtils.remove(serializedHtml, "<head />");

        return serializedHtml;
    }

    private static CleanerProperties createCleanerProperties() {
        CleanerProperties cleanerProperties = new CleanerProperties();

        cleanerProperties.setAllowHtmlInsideAttributes(false);
        cleanerProperties.setAllowMultiWordAttributes(false);
        cleanerProperties.setAddNewlineToHeadAndBody(false);
        cleanerProperties.setKeepWhitespaceAndCommentsInHead(false);
        cleanerProperties.setNamespacesAware(true);
        cleanerProperties.setOmitComments(true);
        cleanerProperties.setOmitDoctypeDeclaration(true);
        cleanerProperties.setOmitXmlDeclaration(true);
        cleanerProperties.setPruneTags("head,meta,title,script,style");
        cleanerProperties.setRecognizeUnicodeChars(true);

        return cleanerProperties;
    }

}
