package nl.wizenoze.justext.html;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CommentNode;
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
        if ( !isMinimizedTagSyntax(tagNode) ) {
            ListIterator<? extends BaseToken> childrenIt = tagChildren.listIterator();
            while ( childrenIt.hasNext() ) {
                Object item = childrenIt.next();
                if (item != null) {
                    if ( item instanceof ContentNode) {
                        String content = ((ContentNode) item).getContent();
                        writer.write( dontEscape(tagNode) ? content.replaceAll("]]>", "]]&gt;") : escapeXml(content) );
                    } else if (item instanceof TagNode) {
                        TagNode tagNodeItem = (TagNode)item;

                        if (!"head".equalsIgnoreCase(tagNodeItem.getName())) {
                            tagNodeItem.serialize(this, writer);
                        }
                    } else {
                        ((BaseToken)item).serialize(this, writer);
                    }
                }
            }

            serializeEndTag(tagNode, writer, false);
        }
    }

}
