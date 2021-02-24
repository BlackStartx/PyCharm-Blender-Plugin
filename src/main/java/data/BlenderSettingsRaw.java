package data;

import com.intellij.util.xmlb.Converter;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

public class BlenderSettingsRaw {
    @OptionTag(converter = BlenderSettingsRaw.BlenderSettingsConverter.class)
    private BlenderSettings blenderSettings;

    public BlenderSettings getBlenderSettings() {
        if (blenderSettings == null) blenderSettings = new BlenderSettings();
        return blenderSettings;
    }

    public static class BlenderSettingsConverter extends Converter<BlenderSettings> {

        @Nullable
        @Override
        public BlenderSettings fromString(@NotNull String value) {
            StringReader reader = new StringReader(value);
            return JAXB.unmarshal(reader, BlenderSettings.class);
        }

        @Override
        public @Nullable String toString(@NotNull BlenderSettings value) {
            StringWriter sw = new StringWriter();
            JAXB.marshal(value, sw);
            return sw.toString();
        }
    }
}
