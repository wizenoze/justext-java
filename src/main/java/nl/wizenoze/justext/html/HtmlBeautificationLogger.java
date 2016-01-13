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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.audit.ErrorType;
import org.htmlcleaner.audit.HtmlModificationListener;
import org.htmlcleaner.conditional.ITagNodeCondition;

/**
 * Created by lcsontos on 1/13/16.
 */
class HtmlBeautificationLogger implements HtmlModificationListener {

    private static final Log LOG = LogFactory.getLog(HtmlBeautificationLogger.class);

    public void fireConditionModification(ITagNodeCondition condition, TagNode tagNode) {
        LOG.info("fireConditionModification:" + condition + " at " + tagNode);
    }

    public void fireHtmlError(boolean safety, TagNode tagNode, ErrorType errorType) {
        LOG.info("fireHtmlError:" + errorType + "(" + safety + ") at " + tagNode);
    }

    public void fireUglyHtml(boolean safety, TagNode tagNode, ErrorType errorType) {
        LOG.info("fireConditionModification:" + errorType + "(" + safety + ") at " + tagNode);
    }

    public void fireUserDefinedModification(boolean safety, TagNode tagNode, ErrorType errorType) {
        LOG.info("fireConditionModification" + errorType + "(" + safety + ") at " + tagNode);
    }

}
