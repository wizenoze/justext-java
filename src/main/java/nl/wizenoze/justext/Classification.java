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

/**
 * After the segmentation and preprocessing, context-free classification is executed which assigns each block to one of
 * four classes: {@link #BAD}, {@link #GOOD}, {@link #NEAR_GOOD} and {@link #SHORT}.
 * @author László Csontos
 */
public enum Classification {

        /**
         * bad -- boilerplate blocks.
         */
        BAD,

        /**
         * good -- main content blocks.
         */
        GOOD,

        /**
         * near-good -- somewhere in-between {@link #SHORT} and {@link #GOOD}.
         */
        NEAR_GOOD,

        /**
         * short -- too short to make a reliable decision about the class.
         */
        SHORT;

}
