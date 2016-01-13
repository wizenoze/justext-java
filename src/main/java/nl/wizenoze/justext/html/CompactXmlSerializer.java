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
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;

/**
 * Created by lcsontos on 1/13/16.
 */
class CompactXmlSerializer extends XmlSerializer {

    CompactXmlSerializer(CleanerProperties cleanerProperties) {
        super(cleanerProperties);
    }

    @Override
    protected void serialize(TagNode tagNode, Writer writer) throws IOException {
        serializeOpenTag(tagNode, writer, false);

        List<? extends BaseToken> tagChildren = tagNode.getAllChildren();

        if (!isMinimizedTagSyntax(tagNode)) {
            Iterator<? extends BaseToken> childrenIterator = tagChildren.listIterator();

            while (childrenIterator.hasNext()) {
                BaseToken node = childrenIterator.next();

                if (node == null) {
                    continue;
                }

                if (node instanceof ContentNode) {
                    String content = ((ContentNode) node).getContent();

                    if (dontEscape(tagNode)) {
                        content = content.replaceAll("]]>", "]]&gt;");
                    } else {
                        content = escapeXml(content);
                    }

                    writer.write(content);
                } else if (node instanceof TagNode) {
                    TagNode tagNodeItem = (TagNode) node;

                    if (!"head".equalsIgnoreCase(tagNodeItem.getName())) {
                        tagNodeItem.serialize(this, writer);
                    }
                } else {
                    node.serialize(this, writer);
                }
            }

            serializeEndTag(tagNode, writer, false);
        }
    }

}
