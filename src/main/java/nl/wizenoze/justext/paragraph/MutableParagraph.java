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

import java.util.List;

import nl.wizenoze.justext.Classification;

/**
 * Created by lcsontos on 1/14/16.
 */
public interface MutableParagraph extends Paragraph {

    /**
     * Add the given text to the existing text nodes.
     * @param text text to be added.
     * @return normalized text of the input which has just been added to the paragraph.
     */
    String appendText(String text);

    /**
     * Decrements tags count.
     */
    void decrementTagsCount();

    /**
     * Creates an immutable copy of this paragraph.
     * @return an immutable copy of this paragraph.
     */
    Paragraph freeze();

    /**
     * Gets text nodes.
     * @return text nodes.
     */
    List<String> getTextNodes();

    /**
     * Increments character count in links.
     * @param delta delta
     */
    void incrementCharsInLinksCount(int delta);

    /**
     * Increments tags count.
     */
    void incrementTagsCount();

    /**
     * Sets classification.
     * @param classification classification.
     */
    void setClassification(Classification classification);

}
