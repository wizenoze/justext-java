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

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lcsontos on 1/14/16.
 */
abstract class BaseParagraph implements Paragraph {

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        return doEquals((Paragraph) obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText(), getXpath());
    }

    boolean doEquals(Paragraph paragraph) {
        return Objects.equals(getText(), paragraph.getText()) && Objects.equals(getXpath(), paragraph.getXpath());
    }

    int getStopWordsCount(String[] words, Set<String> stopWords) {
        int stopWordsCount = 0;

        for (String word : words) {
            if (stopWords.contains(word.toLowerCase())) {
                stopWordsCount++;
            }
        }

        return stopWordsCount;
    }

    float getStopWordsDensity(String[] words, Set<String> stopWords) {
        int wordsCount = words.length;

        if (wordsCount == 0) {
            return 0;
        }

        float stopWordsDensity = 1.0f * getStopWordsCount(words, stopWords) / wordsCount;

        return stopWordsDensity;
    }

    boolean hasText(String text) {
        return StringUtils.isNotBlank(text);
    }

}
