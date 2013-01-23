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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.sound.sampled.AudioFormat;
import org.sound.sampled.AudioFormats;

public abstract class TMatrixFormatConversionProvider extends TSimpleFormatConversionProvider {

    private Map<AudioFormat, List<AudioFormat.Encoding>> targetEncodingsFromSourceFormat;
    private Map<AudioFormat, Map<AudioFormat.Encoding, Collection<AudioFormat>>> targetFormatsFromSourceFormat;

    /**
     *
     * @param sourceFormats
     * @param targetFormats
     * @param abConversionPossible
     */
    protected TMatrixFormatConversionProvider(List<AudioFormat> sourceFormats, List<AudioFormat> targetFormats, boolean[][] abConversionPossible) {
        super(sourceFormats, targetFormats);

        targetEncodingsFromSourceFormat = new HashMap<>();
        targetFormatsFromSourceFormat = new HashMap<>();

        for (int nSourceFormat = 0; nSourceFormat < sourceFormats.size(); nSourceFormat++) {
            AudioFormat sourceFormat = sourceFormats.get(nSourceFormat);

            List<AudioFormat.Encoding> supportedTargetEncodings = new ArrayList<>();

            targetEncodingsFromSourceFormat.put(sourceFormat, supportedTargetEncodings);

            Map<AudioFormat.Encoding, Collection<AudioFormat>> targetFormatsFromTargetEncodings = new HashMap<>();

            targetFormatsFromSourceFormat.put(sourceFormat, targetFormatsFromTargetEncodings);

            for (int nTargetFormat = 0; nTargetFormat < targetFormats.size(); nTargetFormat++) {

                AudioFormat targetFormat = targetFormats.get(nTargetFormat);
                if (abConversionPossible[nSourceFormat][nTargetFormat] == true) {

                    AudioFormat.Encoding targetEncoding = targetFormat.getEncoding();
                    supportedTargetEncodings.add(targetEncoding);
                    Collection<AudioFormat> supportedTargetFormats = targetFormatsFromTargetEncodings.get(targetEncoding);
                    if (supportedTargetFormats == null) {
                        supportedTargetFormats = new ArrayList<>();
                        targetFormatsFromTargetEncodings.put(targetEncoding, supportedTargetFormats);
                    }
                    supportedTargetFormats.add(targetFormat);
                }
            }
        }
    }

    /**
     *
     * @param sourceFormat
     * @return
     */
    @Override
    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat sourceFormat) {
        for (Entry<AudioFormat, List<AudioFormat.Encoding>> entry : targetEncodingsFromSourceFormat.entrySet()) {
            AudioFormat format = entry.getKey();
            if (AudioFormats.matches(format, sourceFormat)) {
                List<AudioFormat.Encoding> targetEncodings = entry.getValue();
                return targetEncodings.toArray(EMPTY_ENCODING_ARRAY);
            }
        }
        return EMPTY_ENCODING_ARRAY;
    }

    /**
     *
     * @param targetEncoding
     * @param sourceFormat
     * @return
     */
    @Override
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat) {
        for (Entry<AudioFormat, Map<AudioFormat.Encoding, Collection<AudioFormat>>> entry : targetFormatsFromSourceFormat.entrySet()) {
            AudioFormat format = entry.getKey();
            if (AudioFormats.matches(format, sourceFormat)) {
                Map<AudioFormat.Encoding, Collection<AudioFormat>> targetEncodings = entry.getValue();
                Collection<AudioFormat> targetFormats = targetEncodings.get(targetEncoding);
                if (targetFormats != null) {
                    return targetFormats.toArray(EMPTY_FORMAT_ARRAY);
                }
                return EMPTY_FORMAT_ARRAY;
            }
        }
        return EMPTY_FORMAT_ARRAY;
    }
}