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

/**
 * Property holder for the context free classifier algorithm.
 *
 * @author László Csontos
 */
public final class ClassifierProperties {

    /**
     * Default threshold for dividing the blocks by length into medium-size and long.
     */
    public static final int LENGTH_HIGH_DEFAULT = 200;

    /**
     * Default threshold for dividing the blocks by length into short and medium-size.
     */
    public static final int LENGTH_LOW_DEFAULT = 70;

    /**
     * Short and near-good headings within MAX_HEADING_DISTANCE characters before good paragraph are classified as good
     * unless {@link #noHeadings} is specified.
     */
    public static final int MAX_HEADING_DISTANCE_DEFAULT = 200;

    /**
     * The link density is defined as the proportion of characters inside <code>&lt;a&gt;</code> tags. If it's greater
     * than {@link #MAX_LINK_DENSITY_DEFAULT} text block is classified as {@link Classification#BAD}.
     */
    public static final float MAX_LINK_DENSITY_DEFAULT = 0.2f;

    /**
     * Take headings into account by default.
     */
    public static final boolean NO_HEADINGS_DEFAULT = false;

    /**
     * Divide the blocks by the stop words density into medium and high.
     */
    public static final float STOPWORDS_HIGH_DEFAULT = 0.32f;

    /**
     * Divide the blocks by the stop words density into low and medium.
     */
    public static final float STOPWORDS_LOW_DEFAULT = 0.30f;

    private final int lengthHigh;
    private final int lengthLow;
    private final int maxHeadingDistance;
    private final float maxLinkDensity;
    private final boolean noHeadings;
    private final float stopwordsHigh;
    private final float stopwordsLow;

    private ClassifierProperties(
            int lengthHigh, int lengthLow, int maxHeadingDistance, float maxLinkDensity, boolean noHeadings,
            float stopwordsHigh, float stopwordsLow) {

        this.lengthHigh = lengthHigh;
        this.lengthLow = lengthLow;
        this.maxHeadingDistance = maxHeadingDistance;
        this.maxLinkDensity = maxLinkDensity;
        this.noHeadings = noHeadings;
        this.stopwordsHigh = stopwordsHigh;
        this.stopwordsLow = stopwordsLow;
    }

    /**
     * Builds a new instance of {@link ClassifierProperties} with its default settings.
     * @return new instance of {@link ClassifierProperties} with its default settings.
     */
    public static ClassifierProperties getDefault() {
        return new Builder().build();
    }

    /**
     * Gets length's upper threshold.
     * @return length's upper threshold.
     */
    public int getLengthHigh() {
        return lengthHigh;
    }

    /**
     * Gets length's lower threshold.
     * @return length's lower threshold.
     */
    public int getLengthLow() {
        return lengthLow;
    }

    /**
     * Gets maximum heading distance.
     * @return  maximum heading distance.
     */
    public int getMaxHeadingDistance() {
        return maxHeadingDistance;
    }

    /**
     * Gets maximum link density.
     * @return maximum link density.
     */
    public float getMaxLinkDensity() {
        return maxLinkDensity;
    }

    /**
     * Returns if headings should be taken into account or not.
     * @return true if headings should be omitted.
     */
    public boolean getNoHeadings() {
        return noHeadings;
    }

    /**
     * Gets stop word's upper threshold.
     * @return stop word's upper threshold.
     */
    public float getStopwordsHigh() {
        return stopwordsHigh;
    }

    /**
     * Gets stop word's lower threshold.
     * @return stop word's lower threshold.
     */
    public float getStopwordsLow() {
        return stopwordsLow;
    }

    /**
     * Builds {@link ClassifierProperties}.
     */
    public static final class Builder {

        private int lengthHigh;
        private int lengthLow;
        private int maxHeadingDistance;
        private float maxLinkDensity;
        private boolean noHeadings;
        private float stopwordsHigh;
        private float stopwordsLow;

        /**
         *
         */
        public Builder() {
            lengthHigh = LENGTH_HIGH_DEFAULT;
            lengthLow = LENGTH_LOW_DEFAULT;
            maxHeadingDistance = MAX_HEADING_DISTANCE_DEFAULT;
            maxLinkDensity = MAX_LINK_DENSITY_DEFAULT;
            noHeadings = NO_HEADINGS_DEFAULT;
            stopwordsHigh = STOPWORDS_HIGH_DEFAULT;
            stopwordsLow = STOPWORDS_LOW_DEFAULT;
        }

        /**
         * Builds an instance of {@link ClassifierProperties}.
         * @return an instance of {@link ClassifierProperties}.
         */
        public ClassifierProperties build() {
            return new ClassifierProperties(
                lengthHigh, lengthLow, maxHeadingDistance, maxLinkDensity, noHeadings, stopwordsHigh, stopwordsLow);
        }

        /**
         * Sets length's upper threshold.
         * @param lengthHigh length's upper threshold.
         * @return builder.
         */
        public Builder setLengthHigh(int lengthHigh) {
            this.lengthHigh = lengthHigh;
            return this;
        }

        /**
         * Sets length's lower threshold.
         * @param lengthLow length's upper threshold.
         * @return builder.
         */
        public Builder setLengthLow(int lengthLow) {
            this.lengthLow = lengthLow;
            return this;
        }

        /**
         * Sets maximum heading distance.
         * @param  maxHeadingDistance maximum heading distance.
         * @return builder.
         */
        public Builder setMaxHeadingDistance(int maxHeadingDistance) {
            this.maxHeadingDistance = maxHeadingDistance;
            return this;
        }

        /**
         * Sets maximum link density.
         * @param  maxLinkDensity maximum link density.
         * @return builder.
         */
        public Builder setMaxLinkDensity(float maxLinkDensity) {
            this.maxLinkDensity = maxLinkDensity;
            return this;
        }

        /**
         * Sets if headings should be taken into account or not.
         * @param noHeadings true if headings should be omitted.
         * @return builder.
         */
        public Builder setNoHeadings(boolean noHeadings) {
            this.noHeadings = noHeadings;
            return this;
        }

        /**
         * Sets stop word's upper threshold.
         * @param stopwordsHigh stop word's upper threshold.
         * @return builder.
         */
        public Builder setStopwordsHigh(float stopwordsHigh) {
            this.stopwordsHigh = stopwordsHigh;
            return this;
        }

        /**
         * Sets stop word's lower threshold.
         * @param stopwordsLow stop word's upper threshold.
         * @return builder.
         */
        public Builder setStopwordsLow(float stopwordsLow) {
            this.stopwordsLow = stopwordsLow;
            return this;
        }

    }

}
