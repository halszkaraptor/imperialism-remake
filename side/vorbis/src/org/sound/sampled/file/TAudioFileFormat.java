/*
 * Copyright (C) 1999 Matthias Pfisterer
 *               2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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
package org.sound.sampled.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

/**
 * This class is just to have a public constructor taking the number of bytes of
 * the whole file. The public constructor of AudioFileFormat doesn't take this
 * parameter, the one who takes it is protected.
 */
public class TAudioFileFormat extends AudioFileFormat {

    private Map<String, Object> m_properties;
    private Map<String, Object> m_unmodifiableProperties;


    /**
     *	Note that the order of the arguments is different from
     *	the one in AudioFileFormat.
     */
    public TAudioFileFormat(Type type, AudioFormat audioFormat, int nLengthInFrames,
            int nLengthInBytes) {
        super(type, nLengthInBytes, audioFormat, nLengthInFrames);
    }

    public TAudioFileFormat(Type type, AudioFormat audioFormat, int nLengthInFrames,
            int nLengthInBytes, Map<String, Object> properties) {
        super(type, nLengthInBytes, audioFormat, nLengthInFrames);
        initMaps(properties);
    }

    private void initMaps(Map<String, Object> properties) {
        /* Here, we make a shallow copy of the map. It's unclear if this
         is sufficient (of if a deep copy should be made).
         */
        m_properties = new HashMap<>();
        m_properties.putAll(properties);
        m_unmodifiableProperties = Collections.unmodifiableMap(m_properties);
    }

    @Override
    public Map<String, Object> properties() {
        return m_unmodifiableProperties;
    }

    protected void setProperty(String key, Object value) {
        m_properties.put(key, value);
    }
}