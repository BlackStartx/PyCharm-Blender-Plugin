package data;

import util.MyProjectHolder;
import util.core.MyIterator;
import com.intellij.openapi.project.Project;
import services.BlendCharmPersistentData;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;

public class BlenderSettings {

    private static BlenderSettings blenderSettings;

    public static BlenderSettings getBlenderSettings(MyProjectHolder project) {
        return getBlenderSettings(project.getProject());
    }

    public static BlenderSettings getBlenderSettings(Project project) {
        if (blenderSettings == null)
            blenderSettings = BlendCharmPersistentData.getInstance(project).getState().getBlenderSettings();
        return blenderSettings;
    }

    @XmlElement(name = "blenderPath")
    private ArrayList<BlenderInstance> blenderPaths;

    @XmlElement(name = "blenderAddonNames")
    private ArrayList<String> blenderAddOnNames;

    public void addBlenderInstance(BlenderInstance instance) {
        if (blenderPaths == null) blenderPaths = new ArrayList<>();
        blenderPaths.add(instance);
    }

    public ArrayList<BlenderInstance> getBlenderInstances() {
        if (blenderPaths == null) return new ArrayList<>();
        return blenderPaths;
    }

    public void removeBlenderInstances(BlenderInstance blenderInstance) {
        if (blenderPaths == null) return;
        blenderPaths.remove(blenderInstance);
    }

    public void addBlenderAddon(String name) {
        if (blenderAddOnNames == null) blenderAddOnNames = new ArrayList<>();
        blenderAddOnNames.add(name);
    }

    public boolean isBlenderAddon(String name) {
        if (blenderAddOnNames == null) return false;
        return blenderAddOnNames.contains(name);
    }

    public void removeBlenderAddon(String name) {
        if (blenderAddOnNames == null) return;
        blenderAddOnNames.remove(name);
    }

    public void removeDeletedAddon(MyProjectHolder project) {
        if (blenderAddOnNames == null) return;

        if (project.projectVirtualFile == null) return;
        blenderAddOnNames = new MyIterator<>(blenderAddOnNames).where(element -> project.projectVirtualFile.findChild(element) != null).toArrayList();
    }

    public ArrayList<String> getBlenderAddons() {
        if (blenderAddOnNames == null) return new ArrayList<>();
        return blenderAddOnNames;
    }
}
