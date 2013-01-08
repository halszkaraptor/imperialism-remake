/*
 * Copyright (C) 2013 Trilarion
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
package org.iremake.xml;

import java.io.IOException;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;
import org.tools.xml.XMLHelper;
import org.tools.xml.XProperty;

/**
 *
 */
public class DefaultOptionsXMLGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        XProperty options = new XProperty(20);
        options.put("graphics.mode.fullscreen", "true");
        options.put("graphics.mainscreen.controlsright", "true");
        options.put("version", "0.1.2");

        Resource resource = ResourceUtils.asResource("options.default.xml");
        XMLHelper.write(resource, options);
    }
}
