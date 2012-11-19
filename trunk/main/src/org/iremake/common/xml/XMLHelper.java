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
package org.iremake.common.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;

/**
 * Read/Write to file.
 */
public class XMLHelper {

    private static final Logger LOG = Logger.getLogger(XMLHelper.class.getName());

    /**
     * No instantiation.
     */
    private XMLHelper() {
    }

    /**
     * Low level reading to XML.
     *
     * @param in Not closed afterwards.
     * @return
     * @throws ParsingException
     * @throws ValidityException
     * @throws IOException
     */
    public static Element read(InputStream in) throws ParsingException, ValidityException, IOException {
        Builder parser = new Builder();
        Document document = parser.build(in);
        return document.getRootElement();
    }

    /**
     * High level reading to XML.
     *
     * @param location
     * @return
     */
    public static Element read(String location) {
        // TODO seems no exception is thrown when not existing (FileNotFound)
        Element root = null;
        try {
            Resource resource = ResourceUtils.asResource(location);
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    root = XMLHelper.read(in);
                }
            }
        } catch (ParsingException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return root;
    }

    /**
     * Convenience method, the XMLable must be created before.
     *
     * @param location
     * @param target
     */
    public static void read(String location, XMLable target) {
        Element xml = XMLHelper.read(location);
        target.fromXML(xml);
    }


    /**
     * Low level writing of XML.
     *
     * @param out Not closed afterwards.
     * @param root
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void write(OutputStream out, Element root) throws UnsupportedEncodingException, IOException {
        Document document = new Document(root);

        Serializer serializer = new Serializer(out, "UTF-8");
        serializer.setIndent(1);
        serializer.setMaxLength(80);
        serializer.write(document);
    }

    /**
     * High level writing.
     *
     * @param location
     * @param root
     */
    public static void write(String location, Element root) {
        // get resource and write to it
        Resource resource;
        try {
            resource = ResourceUtils.asResource(location);
            try (OutputStream out = resource.getOutputStream()) {
                XMLHelper.write(out, root);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Convenience method.
     * @param location
     * @param target
     */
    public static void write(String location, XMLable target) {
        Element xml = target.toXML();
        XMLHelper.write(location, xml);
    }
}