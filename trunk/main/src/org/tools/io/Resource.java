/*
 * Copyright (C) 2010-11 Trilarion
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;

/**
 * Resources function as abstract files for us. They provide input and output
 * streams for io activity. Whether a file is located in the file system or
 * inside an archive (jar/zip) or in the memory is hidden by this framework.
 *
 * Additional functionality includes an existence-check, possibility to clear or
 * create new, and list other files (contained in a directory).
 */
public interface Resource {

    /**
     * A standard comparator used for simple ordering. It redirects to the
     * compareTo method of the getName components.
     */
    public static final Comparator<Resource> comparator = new Comparator<Resource>() {
        @Override
        public int compare(Resource r1, Resource r2) {
            String s1 = r1.getName();
            String s2 = r2.getName();
            if (s1 == null) {
                return -1;
            }
            return s1.compareTo(s2);
        }
    };

    /**
     * Existing or not.
     *
     * @return True if the Resource corresponds to any existing resource.
     */
    public boolean exists();

    /**
     * Create new resource. Existence should then be guaranteed. If already
     * existing nothing is done.
     *
     * @throws IOException If the creation failed.
     */
    public void createNew() throws IOException;

    /**
     * Deletes a resource if it is existing and can be deleted. Otherwise
     * nothing is done.
     *
     * @return True if file was deleted, otherwise false.
     */
    public boolean delete();

    /**
     * Returns a full representation of the Resource including all path
     * elements. Even if the resource is not yet existing.
     *
     * @return
     */
    public String getPath();

    /**
     * Returns a meaningful string representation of the Resource. File names,
     * entries names in archives, ... in most cases without path. Used in the
     * comparator.
     *
     * @return A name
     */
    public String getName();

    /**
     * Returns an input stream.
     *
     * @return The input stream.
     * @throws IOException If it wasn't possible.
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Returns an output stream.
     *
     * @return The output stream.
     * @throws IOException If not possible (e.g. if the resource does not
     * exist).
     */
    public OutputStream getOutputStream() throws IOException;

    /**
     * If the resource is kind of a directory it can search its children for
     * names matching the given regular expression, creating resources from them
     * as a return. Does not include child directories recursively.
     *
     * @param regex Any regular expression string.
     * @return A list of resources.
     * @throws IOException If not applicable (e.g. resource is a file) or on any
     * other error.
     */
    public List<Resource> list(String regex) throws IOException;
}