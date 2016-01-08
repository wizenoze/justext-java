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

import java.util.List;
import java.util.Set;

/**
 * This is the implementation of the following context free classification algorithm.
 *
 * @author László Csontos
 * @see http://corpus.tools/wiki/Justext/Algorithm
 */
public final class Classifier {

    private Classifier() {
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     */
    public static void classifyParagraphs(List<Paragraph> paragraphs, Set<String> stopWords) {
        classifyParagraphs(paragraphs, stopWords, ClassifierProperties.getDefault());
    }

    /**
     * Performs context free classification.
     *
     * @param paragraphs List of paragraphs.
     * @param stopWords Set of stop words.
     * @param classifierProperties Properties.
     */
    public static void classifyParagraphs(
            List<Paragraph> paragraphs, Set<String> stopWords, ClassifierProperties classifierProperties) {
    }

}
