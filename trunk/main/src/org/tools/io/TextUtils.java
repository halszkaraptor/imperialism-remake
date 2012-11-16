/*
 * Copyright (C) 2012 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.tools.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple TextUtils Loader class for ASCII(?) files.
 */
// TODO encoding?
public class TextUtils {

    private static final Logger LOG = Logger.getLogger(TextUtils.class.getName());

    private TextUtils() {
    }

    /**
     *
     * @param in
     * @return
     */
    public static String read(InputStream in) {
        Reader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder(8192);
        char[] buffer = new char[8192];
        int read;
        try {
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return builder.toString();
    }
}