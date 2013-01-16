/*
 * Copyright (C) 2013 Trilarion
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
package org.sound.sampled.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import org.sound.sampled.AudioFormats;

public abstract class TMatrixFormatConversionProvider extends TSimpleFormatConversionProvider {

    private Map<AudioFormat, List<AudioFormat.Encoding>> targetEncodingsFromSourceFormat;
    private Map targetFormatsFromSourceFormat;

    protected TMatrixFormatConversionProvider(List<AudioFormat> sourceFormats, List<AudioFormat> targetFormats, boolean[][] abConversionPossible) {
        super(sourceFormats, targetFormats);

        targetEncodingsFromSourceFormat = new HashMap<>();
        targetFormatsFromSourceFormat = new HashMap();

        for (int nSourceFormat = 0; nSourceFormat < sourceFormats.size(); nSourceFormat++) {
            AudioFormat sourceFormat = sourceFormats.get(nSourceFormat);

            List<AudioFormat.Encoding> supportedTargetEncodings = new ArrayList<>();

            targetEncodingsFromSourceFormat.put(sourceFormat, supportedTargetEncodings);

            Map<AudioFormat.Encoding, Collection<AudioFormat>>  targetFormatsFromTargetEncodings = new HashMap<>();

            targetFormatsFromSourceFormat.put(sourceFormat, targetFormatsFromTargetEncodings);

            for (int nTargetFormat = 0; nTargetFormat < targetFormats.size(); nTargetFormat++) {
                AudioFormat targetFormat = (AudioFormat) targetFormats.get(nTargetFormat);
                if (abConversionPossible[nSourceFormat][nTargetFormat] == true) {
                    AudioFormat.Encoding targetEncoding = targetFormat.getEncoding();
                    supportedTargetEncodings.add(targetEncoding);
                    Collection<AudioFormat> supportedTargetFormats = (Collection<AudioFormat>) targetFormatsFromTargetEncodings.get(targetEncoding);
                    if (supportedTargetFormats == null) {
                        supportedTargetFormats = new ArrayList<>();
                        targetFormatsFromTargetEncodings.put(targetEncoding, supportedTargetFormats);
                    }
                    supportedTargetFormats.add(targetFormat);
                }
            }
        }
    }

    @Override
    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat sourceFormat) {
        Iterator iterator = targetEncodingsFromSourceFormat.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            AudioFormat format = (AudioFormat) entry.getKey();
            if (AudioFormats.matches(format, sourceFormat)) {
                Collection targetEncodings = (Collection) entry.getValue();
                return (AudioFormat.Encoding[]) targetEncodings.toArray(EMPTY_ENCODING_ARRAY);
            }
        }

        return EMPTY_ENCODING_ARRAY;
    }

    @Override
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat) {
        Iterator iterator = targetFormatsFromSourceFormat.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            AudioFormat format = (AudioFormat) entry.getKey();
            if (AudioFormats.matches(format, sourceFormat)) {
                Map targetEncodings = (Map) entry.getValue();
                Collection targetFormats = (Collection) targetEncodings.get(targetEncoding);
                if (targetFormats != null) {
                    return (AudioFormat[]) targetFormats.toArray(EMPTY_FORMAT_ARRAY);
                }

                return EMPTY_FORMAT_ARRAY;
            }

        }

        return EMPTY_FORMAT_ARRAY;
    }
}