package services;

import data.BlenderSettingsRaw;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.sun.istack.Nullable;
import org.jetbrains.annotations.NotNull;

@State(name = "BlendCharmPersistentData", storages = {@Storage("BlendCharmSettings.xml")})
public class BlendCharmPersistentData implements PersistentStateComponent<BlenderSettingsRaw> {

    private final BlenderSettingsRaw blenderSettingsRaw = new BlenderSettingsRaw();

    @NotNull
    @Override
    public BlenderSettingsRaw getState() {
        return blenderSettingsRaw;
    }

    @Override
    public void loadState(@NotNull BlenderSettingsRaw blendCharmPersistentData) {
        XmlSerializerUtil.copyBean(blendCharmPersistentData, this.blenderSettingsRaw);
    }

    @Nullable
    public static BlendCharmPersistentData getInstance(Project project) {
        return ServiceManager.getService(project, BlendCharmPersistentData.class);
    }
}