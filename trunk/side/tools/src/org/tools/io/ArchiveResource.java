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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class treats entries in archives (".zip" or ".jar") as an instance of
 * Resource, i.e. input and output streams can be obtained. Given a file which
 * point to an archive and a path inside this archive it obtains ZipFile and
 * ZipEntry objects which are corresponding to this situation.
 *
 * Here we treat archives as read-only repositories!
 *
 * Also archives inside archives do not work!
 *
 * Note that inside a archive the delimiter is always "/" and directories always
 * end with "/" (actually directory entries without "/" do not appear in the
 * ZipFile.entries() enumeration, however ZipFile.getEntry() will not return
 * null in this case, but an entry with field size equals zero - bad coding
 * style inside java.util.zip and the reason why we need the entries listFiles)
 *
 * Also here some checks and optimizations can still be done. First we should
 * check that parallel opening the same archive with another ZipFile instance
 * works and secondly if creating a ZipFile instance is too costly we might
 * consider putting them in a cache / static HashMap..
 *
 * BTW. in Linux file names are case sensitive, in Windows not.
 */
public class ArchiveResource implements Resource {

    private static final Logger LOG = Logger.getLogger(ArchiveResource.class.getName());
    // TODO handling of the path
    /**
     * Representation of an archive which is a ".zip" or ".jar" file in the file
     * system
     */
    private ZipFile archive;
    /**
     * List of all entries in the archive
     */
    private Set<ZipEntry> entries = new HashSet<>(1000);
    /**
     * Entry in the archive we are actually pointing to
     */
    private ZipEntry entry = null;
    /**
     * True if the entry is not a directory, e.g. if the path does not end with
     * "/"
     */
    private boolean isFile;

    /**
     * Given a file pointing to an archive and a path inside this archive,
     * constructs an instance
     */
    ArchiveResource(File file, String path) throws IOException {
        // must exist and must be file
        if (!file.exists() || !file.isFile()) {
            LOG.log(Level.SEVERE, "File {0} not existent or not a file.", file.getPath());
            throw new IOException();
        }
        // create new ZipFile instance
        archive = new ZipFile(file);

        // read content of whole zip file, the root is unfortunately not contained as simple "/" for example
        // so we have to take care of this below
        for (Enumeration<? extends ZipEntry> entrylist = archive.entries(); entrylist.hasMoreElements();) {
            ZipEntry e = entrylist.nextElement();
            entries.add(e);
            // test if its the one we want
            String name = e.getName();
            if (name.equals(path)) {
                entry = e;
            }
        }

        if (path == null || path.isEmpty()) {
            // we only want the central ZipFile, unfortunately there is no entry for it
            // we have to be careful that we later do not mix this with a wrong path
            isFile = false;
        } else {
            if (entry != null) {
                // check if directory or simple file
                isFile = !entry.isDirectory();
            }
        }
    }

    /**
     * Clone an ArchiveResource with the same archive but a different entry in
     * this archive.
     *
     * @param parent The mother Resource
     * @param newEntry The new ZipEntry
     */
    private ArchiveResource(ArchiveResource parent, ZipEntry newEntry) throws IOException {
        archive = parent.archive;
        entries = parent.entries;
        if (entries.contains(newEntry)) {
            entry = newEntry;
            isFile = !entry.isDirectory();
        } else {
            throw new IOException();
        }
    }

    /**
     * {@inheritDoc }
     * Archive and entry inside archive must exist.
     */
    @Override
    public boolean exists() {
        return entry != null;
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void createNew() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     */
    @Override
    public boolean delete() {
        return false;
    }

    /**
     *
     * @return @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        if (!exists() || !isFile) {
            throw new IOException();
        }
        return archive.getInputStream(entry);
    }

    /**
     * {@inheritDoc} Archive are read-only (for us) so we always throw an
     * exception here. You should never want to use this method on an archive
     * resource.
     */
    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException("Writing to archives not supported yet.");
    }

    /**
     * {@inheritDoc} A lot of work has to be check for a feature like this
     * inside archives..
     */
    @Override
    public List<Resource> list(String regex) throws IOException {
        if (!exists() || isFile) {
            throw new IOException();
        }
        // the listFiles we are going to return
        List<Resource> list = new LinkedList<>();
        String path = getFilePath(entry);
        Pattern pattern = Pattern.compile(regex);

        // loop through all entries
        for (ZipEntry e : entries) {
            // directories not allowed
            if (!e.isDirectory()) {
                // should have equal paths
                String p = getFilePath(e);
                if (path.equals(p)) {
                    String name = getFileName(e);
                    if (pattern.matcher(name).matches()) {
                        list.add(new ArchiveResource(this, e));
                    }
                }
            }
        }
        return list;
    }

    /**
     * {@inheritDoc} Handling the root case (entry == null) and removing the
     * path part must be paid attention here.
     */
    @Override
    public String getName() {
        if (!exists()) {
            return null;
        }
        return getFileName(entry);
    }

    /**
     *
     * @return
     */
    @Override
    public String getPath() {
        return archive.getName() + ResourceUtils.Delimiter + entry.getName();
    }

    /**
     * Gets the file name only (without any directory part)
     */
    private static String getFileName(ZipEntry entry) {
        // get the full path
        String name = "";
        // it could be the root (entry == null)
        if (entry != null) {
            name = entry.getName();
            // only return part after the last "/"
            int k = name.lastIndexOf('/');
            if (k != -1) {
                name = name.substring(k + 1);
            }
        }
        return name;
    }

    /**
     * Gets the file path only
     */
    private static String getFilePath(ZipEntry entry) {
        // get the full path
        String name = "";
        // it could be the root (entry == null)
        if (entry != null) {
            name = entry.getName();
            // only return part before (including) the last "/"
            int k = name.lastIndexOf('/');
            if (k != -1) {
                name = name.substring(0, k + 1);
            } else {
                name = "";
            }
        }
        return name;
    }
}
