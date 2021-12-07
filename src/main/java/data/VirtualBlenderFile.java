package data;

import settings.BlenderSettings;
import util.MyProjectHolder;
import util.MyVirtualFileHelper;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

public class VirtualBlenderFile {

    public final MyProjectHolder project;
    private final BlenderSettings settings;

    private final VirtualFile selectedVirtualFile;
    private final VirtualFile subRootVirtualFile;
    private final VirtualFile rootVirtualFile;

    public final boolean isRoot;
    public final boolean isSubRoot;

    public VirtualBlenderFile(MyProjectHolder project, VirtualFile virtualFile) {
        this.project = project;
        this.selectedVirtualFile = virtualFile;

        VirtualFile parent = virtualFile.getParent();

        this.isRoot = parent != null && virtualFile.getPath().equals(project.getBasePath());
        this.isSubRoot = parent != null && parent.getPath().equals(project.getBasePath());
        this.subRootVirtualFile = this.isSubRoot ? virtualFile : MyVirtualFileHelper.getProjectFirstVirtualFile(project, virtualFile);
        this.rootVirtualFile = project.projectVirtualFile;
        this.settings = BlenderSettings.getBlenderSettings(project);
    }

    /*
     *  Data
     */

    public boolean isRootAndBlenderProject() {
        return isRoot && settings.isBlenderProject();
    }

    public boolean isSubRootAndBlenderAddon() {
        return isSubRoot && settings.isBlenderAddon(subRootVirtualFile.getName());
    }

    public boolean isBlenderAddon() {
        return isRootAndBlenderProject() || isSubRootAndBlenderAddon();
    }

    public String getRelativeAddonName() {
        return settings.isBlenderProject() ? rootVirtualFile.getName() : settings.isBlenderAddon(subRootVirtualFile.getName()) ? subRootVirtualFile.getName() : null;
    }

    public void markAsAddonFolder() {
        if (isSubRootAndBlenderAddon()) return;

        settings.addBlenderAddon(subRootVirtualFile.getName());
        sync();
    }

    public void markAsAddonProject() {
        settings.markAsAddonProject();
        sync();
    }

    public void unmarkAsAddonFolder() {
        if (!isSubRootAndBlenderAddon()) return;

        settings.removeBlenderAddon(subRootVirtualFile.getName());
        sync();
    }

    public void unmarkAsAddonProject() {
        settings.unmarkAsAddonProject();
        sync();
    }

    private void sync() {
        project.getProject().save();
    }

    public VirtualFile getSubRootVirtualFile() {
        return subRootVirtualFile;
    }

    public boolean isSource() {
        for (VirtualFile f : ProjectRootManager.getInstance(project.getProject()).getContentSourceRoots()) {
            if (f.equals(selectedVirtualFile))
                return true;
        }
        return false;
    }
}
