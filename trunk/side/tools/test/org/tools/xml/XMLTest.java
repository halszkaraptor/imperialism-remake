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
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.junit.Assert;
import org.junit.Test;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;

/**
 * XML (Property) read/write test.
 */
public class XMLTest {

    private static final Logger LOG = Logger.getLogger(XMLTest.class.getName());

    @Test
    public void PropertyNullTest() {
        XProperty p = new XProperty(10);

        p.put(null, null);

        Element root = p.toXML();
        try {
            XMLHelper.write(System.out, root);
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void PropertyIOTest() throws IOException, ParsingException {

        // create new property
        XProperty p = new XProperty(10);
        p.put("car", "mercedes");
        p.put("pet", "cat");
        p.putInt("age", 21);
        p.putBoolean("single", true);

        // convert to xml
        Element root = p.toXML();

        // write to file
        Resource location = ResourceUtils.asResource("XMLTest.Property.xml");
        OutputStream out = location.getOutputStream();
        XMLHelper.write(out, root);
        out.close();

        InputStream in = location.getInputStream();
        root = XMLHelper.read(in);
        in.close();
        p.fromXML(root);

        // some tests
        Assert.assertTrue(p.size() == 4);
        Assert.assertTrue("mercedes".equals(p.get("car")));
        Assert.assertTrue("cat".equals(p.get("pet")));
        Assert.assertTrue(p.getInt("age") == 21);
        Assert.assertTrue(p.getBoolean("single") == true);

        // TODO this fails, something is not closed correctly

        // delete file again
        Assert.assertTrue(location.delete());

    }
}