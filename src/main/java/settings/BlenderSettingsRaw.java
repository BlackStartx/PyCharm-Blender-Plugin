package settings;

import com.intellij.util.xmlb.Converter;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

public class BlenderSettingsRaw {
    @OptionTag(converter = BlenderSettingsRaw.BlenderSettingsConverter.class)
    private BlenderSettingsData blenderSettings;

    public BlenderSettingsData getBlenderSettings() {
        if (blenderSettings == null) blenderSettings = new BlenderSettingsData();
        return blenderSettings;
    }

    public static class BlenderSettingsConverter extends Converter<BlenderSettingsData> {

        @Nullable
        @Override
        public BlenderSettingsData fromString(@NotNull String value) {
            StringReader reader = new StringReader(value);
            return JAXB.unmarshal(reader, BlenderSettingsData.class);
        }

        @Override
        public @Nullable
        String toString(@NotNull BlenderSettingsData value) {
            StringWriter sw = new StringWriter();
            JAXB.marshal(value, sw);
            return sw.toString();
        }
    }
}
