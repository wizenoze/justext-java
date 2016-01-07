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

import org.jsoup.safety.Whitelist;

/**
 * Built-in singleton, immutable white-list for the jusText algorithm.
 *
 * @see #getInstance()
 * @author László Csontos
 */
public final class TextualWhitelist extends Whitelist {

    /**
     * Private constructor for singleton creation.
     */
    private TextualWhitelist() {
        TextualTag[] textualTags = TextualTag.values();
        String[] tags = new String[textualTags.length];

        for (int index = 0; index < textualTags.length; index++) {
            tags[index] = textualTags[index].name().toLowerCase();
        }

        super.addTags(tags);
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist addAttributes(String tag, String... keys) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist addEnforcedAttribute(String tag, String key, String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist addProtocols(String tag, String key, String... protocols) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist addTags(String... tags) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist preserveRelativeLinks(boolean preserve) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist removeAttributes(String tag, String... keys) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist removeEnforcedAttribute(String tag, String key) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist removeProtocols(String tag, String key, String... protocols) {
        throw new UnsupportedOperationException();
    }

    /**
     * As {@link TextualWhitelist} is singleton and immutable this method throws {@link UnsupportedOperationException}.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public Whitelist removeTags(String... tags) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a singleton, immutable instance in a thread-safe way.
     *
     * @return the singleton instances.
     */
    public static Whitelist getInstance() {
        return TextualWhitelistHolder.INSTANCE;
    }

    private static class TextualWhitelistHolder {
        static final Whitelist INSTANCE = new TextualWhitelist();
    }

}
