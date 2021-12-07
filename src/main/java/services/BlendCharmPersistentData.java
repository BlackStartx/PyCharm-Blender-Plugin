package services;

import settings.BlenderSettingsRaw;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.sun.istack.Nullable;
import org.jetbrains.annotations.NotNull;

@State(name = "BlendCharmPersistentData", storages = {@Storage("BlendCharmSettings.xml")})
public class BlendCharmPersistentData implements PersistentStateComponent<BlenderSettingsRaw> {

    @Nullable
    public static BlendCharmPersistentData getInstance(Project project) {
        return project.getService(BlendCharmPersistentData.class);
    }

    private BlenderSettingsRaw blenderSettingsRaw = new BlenderSettingsRaw();

    @NotNull
    @Override
    public BlenderSettingsRaw getState() {
        return blenderSettingsRaw;
    }

    @Override
    public void loadState(@NotNull BlenderSettingsRaw blendCharmPersistentData) {
        blenderSettingsRaw = blendCharmPersistentData;
    }
}