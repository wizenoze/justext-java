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

import org.htmlcleaner.TagNode;
import org.htmlcleaner.audit.ErrorType;
import org.htmlcleaner.audit.HtmlModificationListener;
import org.htmlcleaner.conditional.ITagNodeCondition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcsontos on 1/13/16.
 */
class HtmlBeautificationLogger implements HtmlModificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlBeautificationLogger.class);

    public void fireConditionModification(ITagNodeCondition condition, TagNode tagNode) {
        log("fireConditionModification", false, condition, tagNode, null);
    }

    public void fireHtmlError(boolean safety, TagNode tagNode, ErrorType errorType) {
        log("fireHtmlError", safety, null, tagNode, errorType);
    }

    public void fireUglyHtml(boolean safety, TagNode tagNode, ErrorType errorType) {
        log("fireUglyHtml", safety, null, tagNode, errorType);
    }

    public void fireUserDefinedModification(boolean safety, TagNode tagNode, ErrorType errorType) {
        log("fireUserDefinedModification", safety, null, tagNode, errorType);
    }

    private void log(
            String evenName, boolean safety, ITagNodeCondition condition, TagNode tagNode, ErrorType errorType) {

        if (!LOG.isDebugEnabled()) {
            return;
        }

        if ("fireConditionModification".equals(evenName)) {
            LOG.debug("{}: {} at {}", evenName, condition, tagNode);
        } else {
            LOG.debug("{}: {} ({}) at {}", evenName, errorType, safety, tagNode);
        }
    }

}
