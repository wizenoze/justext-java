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

package nl.wizenoze.justext.exception;

/**
 * Thrown when HTML or XML related parse errors occur.
 * @author László Csontos
 */
public class JusTextParseException extends JusTextException {

    /**
     * @see RuntimeException#RuntimeException().
     */
    public JusTextParseException() {
        super();
    }

    /**
     * @param cause Cause.
     * @see RuntimeException#RuntimeException(Throwable).
     */
    public JusTextParseException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message Message.
     * @see RuntimeException#RuntimeException(String).
     */
    public JusTextParseException(String message) {
        super(message);
    }

    /**
     * @param message Message.
     * @param cause Cause.
     * @see RuntimeException#RuntimeException(String, Throwable).
     */
    public JusTextParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
