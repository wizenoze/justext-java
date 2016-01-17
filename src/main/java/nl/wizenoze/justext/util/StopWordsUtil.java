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

import java.util.Collections;
import java.util.Set;

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
        // TODO: implement
        return Collections.emptySet();
    }

}
