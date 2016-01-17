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

package nl.wizenoze.justext.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

import nl.wizenoze.justext.exception.JusTextStopWordsException;

/**
 * @author László Csontos
 */
public final class StopWordsUtil {

    private StopWordsUtil() {
    }

    /**
     * Returns a set of stop-words which correspond to the given language code.
     *
     * @param languageCode ISO 639-1 language code.
     * @return set of stop-words which correspond to the given language code.
     */
    public static Set<String> getStopWords(String languageCode) {
        try {
            return stopWords(languageCode);
        } catch (IOException ioe) {
            throw new JusTextStopWordsException(ioe.getMessage(), ioe);
        }
    }

    private static Set<String> stopWords(String languageCode) throws IOException {
        InputStream inputStream = StopWordsUtil.class.getResourceAsStream("/stopwords/" + languageCode);

        // No such resource has been found
        if (inputStream == null) {
            throw new JusTextStopWordsException(String.format("Language code %s doesn't exist", languageCode));
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.toSet());
        }
    }

}
