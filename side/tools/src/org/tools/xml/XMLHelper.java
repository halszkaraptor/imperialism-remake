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
package org.tools.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import org.tools.io.Resource;

/**
 * Read/Write to/from file/resources/variables.
 */
public class XMLHelper {

    /* the logger */
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
     * @throws IOException
     */
    public static Element read(InputStream in) throws IOException, ParsingException {
        Builder parser = new Builder();
        Document document = parser.build(in);
        return document.getRootElement();
    }

    /**
     * High level reading to XML.
     *
     * @param resource
     * @return
     * @throws IOException
     * @throws ParsingException
     */
    public static Element read(Resource resource) throws IOException, ParsingException {
        try (InputStream in = resource.getInputStream()) {
            return XMLHelper.read(in);
        }
    }

    /**
     * Convenience method, the XMLable must be created before.
     *
     * @param resource
     * @param target
     * @throws IOException
     * @throws ParsingException
     */
    public static void read(Resource resource, ReadXMLable target) throws IOException, ParsingException {
        Element xml = XMLHelper.read(resource);
        target.fromXML(xml); // TODO check target not null
    }

    /**
     * Low level writing of XML.
     *
     * @param out Not closed afterwards.
     * @param root
     * @throws IOException
     */
    public static void write(OutputStream out, Element root) throws IOException {

        // get document, if not existing create new with this as the root node
        Document document = root.getDocument();
        if (document == null) {
            document = new Document(root);
        }

        Serializer serializer = new Serializer(out, "UTF-8");
        serializer.setIndent(1);
        serializer.setMaxLength(0); // otherwise spaces are not preserved!
        serializer.write(document);
    }

    /**
     * High level writing.
     *
     * @param resource
     * @param root
     * @throws IOException
     */
    public static void write(Resource resource, Element root) throws IOException {
        try (OutputStream out = resource.getOutputStream()) { // this will create parent directories if they aren't yet available
            XMLHelper.write(out, root);
        }
    }

    /**
     * Convenience method.
     *
     * @param resource
     * @param target
     * @throws IOException
     */
    public static void write(Resource resource, FullXMLable target) throws IOException {
        Element xml = target.toXML();
        XMLHelper.write(resource, xml);
    }
}