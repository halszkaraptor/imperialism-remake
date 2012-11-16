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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of Resource for files. We use java.io.File and distinguish
 * between existing/not existing and is file/is directory.
 *
 * To create a FileResource we already need a File, see ResourceUtils for an
 * automated way how to get a Resource.
 */
public class FileResource implements Resource {

    private File file;

    /**
     * Constructs from a file, test for existence and file/directory property.
     *
     * @param file
     * @throws IOException
     */
    public FileResource(File file) throws IOException {
        this.file = file;
    }

    /**
     * {@inheritDoc }
     * Does the file/directory exist?
     */
    @Override
    public boolean exists() {
        return file.exists();
    }

    /**
     *
     */
    @Override
    public void createNew() throws IOException {
        if (!exists()) {
            // first try to create the parent directory if not already
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdir();
            }
            file.createNewFile();
        }
    }

    /**
     *
     */
    @Override
    public boolean delete() {
        if (exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * {@inheritDoc} Resource must be a file and existing to deliver stream.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        if (!exists() || !file.isFile()) {
            throw new IOException();
        }
        return new FileInputStream(file);
    }

    /**
     * {@inheritDoc} If resource not exists, it is created. In all cases it must
     * be a file to deliver a stream.
     *
     * If the file was not existing and was created it is not deleted, even it
     * is a directory then and the method throws an exception.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        createNew();
        if (!file.isFile()) {
            throw new IOException();
        }
        return new FileOutputStream(file);
    }

    /**
     *
     * @param regex
     * @return
     * @throws IOException
     */
    @Override
    public List<Resource> list(String regex) throws IOException {
        if (!exists() || !file.isDirectory()) {
            throw new IOException();
        }
        List<Resource> list = new LinkedList<>();
        Pattern pattern = Pattern.compile(regex);
        // call listFiles on file for getting all children that are not directories
        File[] files = file.listFiles();
        // traversing the array and filter for our regular expression
        for (File f : files) {
            if (pattern.matcher(f.getName()).matches()) {
                // it could also be an archive, so we have to use ResourceUtils
                list.add(ResourceUtils.asResource(f));
            }
        }
        return list;
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        if (!exists()) {
            return null;
        }
        return file.getName();
    }

    /**
     *
     * @return
     */
    @Override
    public String getPath() {
        return file.getPath();
    }
}
