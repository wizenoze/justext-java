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

import nl.wizenoze.justext.util.StringPool;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Thi class represents a hierarchical path of a certain HTML tag within the document.
 *
 * @author László Csontos
 */
public final class PathInfo {

    private final Deque<PathElement> elements;

    /**
     * Creates an empty {@link PathInfo}.
     */
    public PathInfo() {
        elements = new LinkedList<>();
    }

    /**
     * Appends a tag represented by tagName to the end of its path.
     *
     * @param tagName name of tag to be appended to the current path.
     * @return this object in order to enable clients to chain invocation of {@link #append(String)} and {@link #pop()}.
     */
    public PathInfo append(String tagName) {
        Map<String, Integer> children = getChildren();

        Integer order = children.get(tagName);

        if (order == null) {
            order = 0;
        }

        children.put(tagName, ++order);

        elements.push(new PathElement(tagName, order));

        return this;
    }

    /**
     * Creates a simple string representation of this path object.
     *
     * <code>
     *     PathInfo path = new PathInfo().append("html").append("body").append("div")
     * </code>
     *
     * Results in <code>html.body.div</code>.
     *
     * @return simple string representation of this path object.
     */
    public String dom() {
        if (elements.isEmpty()) {
            return StringPool.EMPTY;
        }

        StringBuilder sb = new StringBuilder();

        PathElement firstElement = elements.peekFirst();
        Iterator<PathElement> elementIterator = elements.descendingIterator();

        while (elementIterator.hasNext()) {
            PathElement element = elementIterator.next();

            sb.append(element.tagName);

            if (element != firstElement) {
                sb.append(StringPool.PERIOD);
            }
        }

        return sb.toString();
    }

    /**
     * Removes the last element from the tail of the current path.
     *
     * @return this object in order to enable clients to chain invocation of {@link #append(String)} and {@link #pop()}.
     */
    public PathInfo pop() {
        elements.pop();

        return this;
    }

    /**
     * Creates the XPath representation of this path object.
     *
     * <code>
     *     PathInfo path = new PathInfo().append("html").append("body").append("div")
     * </code>
     *
     * Results in <code>/html[1]/body[1]/div[1]</code>.
     *
     * @return simple string representation of this path object.
     */
    public String xpath() {
        if (elements.isEmpty()) {
            return StringPool.SLASH;
        }

        StringBuilder sb = new StringBuilder();

        Iterator<PathElement> elementIterator = elements.descendingIterator();

        while (elementIterator.hasNext()) {
            PathElement element = elementIterator.next();

            sb.append(StringPool.SLASH);
            sb.append(element.tagName);
            sb.append(StringPool.OPENING_BRACKET);
            sb.append(element.order);
            sb.append(StringPool.CLOSING_BRACKET);
        }

        return sb.toString();
    }

    private Map<String, Integer> getChildren() {
        if (elements.isEmpty()) {
            return new HashMap<>();
        }

        return elements.peekLast().children;
    }

    private class PathElement {

        private final Map<String, Integer> children;
        private final int order;
        private final String tagName;

        PathElement(String tagName, int order) {
            this(tagName, order, new HashMap<>());
        }

        PathElement(String tagName, int order, Map<String, Integer> children) {
            this.tagName = tagName;
            this.order = order;
            this.children = children;
        }

    }

}
