package settings;

import data.BlenderInstance;
import util.MyProjectHolder;
import com.intellij.openapi.project.Project;
import services.BlendCharmPersistentData;

import java.util.ArrayList;
import java.util.stream.Collectors;

public record BlenderSettings(BlenderSettingsData data) {

    public static BlenderSettings getBlenderSettings(MyProjectHolder project) {
        return getBlenderSettings(project.getProject());
    }

    public static BlenderSettings getBlenderSettings(Project project) {
        return new BlenderSettings(BlendCharmPersistentData.getInstance(project).getState().getBlenderSettings());
    }

    public boolean isBlenderProject() {
        return data.blenderAddOnNames != null && data.blenderAddOnNames.size() == 1 && data.blenderAddOnNames.get(0).equals(".");
    }

    public void addBlenderInstance(BlenderInstance instance) {
        if (data.blenderPaths == null) data.blenderPaths = new ArrayList<>();
        data.blenderPaths.add(instance);
    }

    public ArrayList<BlenderInstance> getBlenderInstances() {
        if (data.blenderPaths == null) return new ArrayList<>();
        return data.blenderPaths;
    }

    public void removeBlenderInstances(BlenderInstance blenderInstance) {
        if (data.blenderPaths == null) return;
        data.blenderPaths.remove(blenderInstance);
    }

    public void addBlenderAddon(String name) {
        if (data.blenderAddOnNames == null) data.blenderAddOnNames = new ArrayList<>();
        data.blenderAddOnNames.add(name);
    }

    public boolean isBlenderAddon(String name) {
        if (data.blenderAddOnNames == null) return false;
        return data.blenderAddOnNames.contains(name);
    }

    public void removeBlenderAddon(String name) {
        if (data.blenderAddOnNames == null) return;
        data.blenderAddOnNames.remove(name);
    }

    public void markAsAddonProject() {
        data.blenderAddOnNames = new ArrayList<>();
        data.blenderAddOnNames.add(".");
    }

    public void unmarkAsAddonProject() {
        data.blenderAddOnNames = new ArrayList<>();
    }

    public void removeDeletedAddon(MyProjectHolder project) {
        if (data.blenderAddOnNames == null || project.projectVirtualFile == null || isBlenderProject()) return;

        data.blenderAddOnNames = data.blenderAddOnNames.stream()
                .filter(element -> project.projectVirtualFile.findChild(element) != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> getBlenderAddons() {
        if (data.blenderAddOnNames == null) return new ArrayList<>();
        return data.blenderAddOnNames;
    }
}
