package share.sampled.convert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import share.ArraySet;
import share.sampled.AudioFormats;

public abstract class TMatrixFormatConversionProvider extends TSimpleFormatConversionProvider {

    private Map m_targetEncodingsFromSourceFormat;
    private Map m_targetFormatsFromSourceFormat;

    protected TMatrixFormatConversionProvider(List sourceFormats, List targetFormats, boolean[][] abConversionPossible) {
        super(sourceFormats, targetFormats);

        m_targetEncodingsFromSourceFormat = new HashMap();
        m_targetFormatsFromSourceFormat = new HashMap();

        for (int nSourceFormat = 0;
                nSourceFormat < sourceFormats.size();
                nSourceFormat++) {
            AudioFormat sourceFormat = (AudioFormat) sourceFormats.get(nSourceFormat);
            List supportedTargetEncodings = new ArraySet();
            m_targetEncodingsFromSourceFormat.put(sourceFormat, supportedTargetEncodings);
            Map targetFormatsFromTargetEncodings = new HashMap();
            m_targetFormatsFromSourceFormat.put(sourceFormat, targetFormatsFromTargetEncodings);
            for (int nTargetFormat = 0;
                    nTargetFormat < targetFormats.size();
                    nTargetFormat++) {
                AudioFormat targetFormat = (AudioFormat) targetFormats.get(nTargetFormat);
                if (abConversionPossible[nSourceFormat][nTargetFormat] == true) {
                    AudioFormat.Encoding targetEncoding = targetFormat.getEncoding();
                    supportedTargetEncodings.add(targetEncoding);
                    Collection supportedTargetFormats = (Collection) targetFormatsFromTargetEncodings.get(targetEncoding);
                    if (supportedTargetFormats == null) {
                        supportedTargetFormats = new ArraySet();
                        targetFormatsFromTargetEncodings.put(targetEncoding, supportedTargetFormats);
                    }
                    supportedTargetFormats.add(targetFormat);
                }
            }
        }
    }

    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat sourceFormat) {
        Iterator iterator = m_targetEncodingsFromSourceFormat.entrySet().iterator();
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

    public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat) {
        Iterator iterator = m_targetFormatsFromSourceFormat.entrySet().iterator();
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