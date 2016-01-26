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

package nl.wizenoze.justext;

import java.math.BigDecimal;
import java.util.Objects;

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
    public static final BigDecimal MAX_LINK_DENSITY_DEFAULT = new BigDecimal("0.2");

    /**
     * Take headings into account by default.
     */
    public static final boolean NO_HEADINGS_DEFAULT = false;

    /**
     * Divide the blocks by the stop words density into medium and high.
     */
    public static final BigDecimal STOP_WORDS_HIGH_DEFAULT = new BigDecimal("0.32");

    /**
     * Divide the blocks by the stop words density into low and medium.
     */
    public static final BigDecimal STOP_WORDS_LOW_DEFAULT = new BigDecimal("0.30");

    private final int lengthHigh;
    private final int lengthLow;
    private final int maxHeadingDistance;
    private final BigDecimal maxLinkDensity;
    private final boolean noHeadings;
    private final BigDecimal stopWordsHigh;
    private final BigDecimal stopWordsLow;

    private ClassifierProperties(
            int lengthHigh, int lengthLow, int maxHeadingDistance, BigDecimal maxLinkDensity, boolean noHeadings,
            BigDecimal stopWordsHigh, BigDecimal stopWordsLow) {

        this.lengthHigh = lengthHigh;
        this.lengthLow = lengthLow;
        this.maxHeadingDistance = maxHeadingDistance;
        this.maxLinkDensity = maxLinkDensity;
        this.noHeadings = noHeadings;
        this.stopWordsHigh = stopWordsHigh;
        this.stopWordsLow = stopWordsLow;
    }

    /**
     * Builds a new instance of {@link ClassifierProperties} with its default settings.
     * @return new instance of {@link ClassifierProperties} with its default settings.
     */
    public static ClassifierProperties getDefault() {
        return new Builder().build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof ClassifierProperties)) {
            return false;
        }

        ClassifierProperties classifierProperties = (ClassifierProperties) obj;

        return Objects.equals(lengthHigh, classifierProperties.lengthHigh)
                && Objects.equals(lengthLow, classifierProperties.lengthLow)
                && Objects.equals(maxHeadingDistance, classifierProperties.maxHeadingDistance)
                && Objects.equals(maxLinkDensity, classifierProperties.maxLinkDensity)
                && Objects.equals(noHeadings, classifierProperties.noHeadings)
                && Objects.equals(stopWordsHigh, classifierProperties.stopWordsHigh)
                && Objects.equals(stopWordsLow, classifierProperties.stopWordsLow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                lengthHigh, lengthLow, maxHeadingDistance, maxLinkDensity, noHeadings, stopWordsHigh, stopWordsLow);
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
    public BigDecimal getMaxLinkDensity() {
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
    public BigDecimal getStopWordsHigh() {
        return stopWordsHigh;
    }

    /**
     * Gets stop word's lower threshold.
     * @return stop word's lower threshold.
     */
    public BigDecimal getStopWordsLow() {
        return stopWordsLow;
    }

    /**
     * Builds {@link ClassifierProperties}.
     */
    public static final class Builder {

        private int lengthHigh;
        private int lengthLow;
        private int maxHeadingDistance;
        private BigDecimal maxLinkDensity;
        private boolean noHeadings;
        private BigDecimal stopWordsHigh;
        private BigDecimal stopWordsLow;

        /**
         *
         */
        public Builder() {
            lengthHigh = LENGTH_HIGH_DEFAULT;
            lengthLow = LENGTH_LOW_DEFAULT;
            maxHeadingDistance = MAX_HEADING_DISTANCE_DEFAULT;
            maxLinkDensity = MAX_LINK_DENSITY_DEFAULT;
            noHeadings = NO_HEADINGS_DEFAULT;
            stopWordsHigh = STOP_WORDS_HIGH_DEFAULT;
            stopWordsLow = STOP_WORDS_LOW_DEFAULT;
        }

        /**
         * Builds an instance of {@link ClassifierProperties}.
         * @return an instance of {@link ClassifierProperties}.
         */
        public ClassifierProperties build() {
            if (lengthHigh < lengthLow) {
                throw new IllegalStateException("lengthHigh < lengthLow");
            }

            if (stopWordsHigh.compareTo(stopWordsLow) < 0) {
                throw new IllegalStateException("stopWordsHigh < stopWordsLow");
            }

            return new ClassifierProperties(
                lengthHigh, lengthLow, maxHeadingDistance, maxLinkDensity, noHeadings, stopWordsHigh, stopWordsLow);
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
        public Builder setMaxLinkDensity(BigDecimal maxLinkDensity) {
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
         * @param stopWordsHigh stop word's upper threshold.
         * @return builder.
         */
        public Builder setStopWordsHigh(BigDecimal stopWordsHigh) {
            this.stopWordsHigh = stopWordsHigh;
            return this;
        }

        /**
         * Sets stop word's lower threshold.
         * @param stopWordsLow stop word's upper threshold.
         * @return builder.
         */
        public Builder setStopWordsLow(BigDecimal stopWordsLow) {
            this.stopWordsLow = stopWordsLow;
            return this;
        }

    }

}
