package data;

import util.MyProjectHolder;
import util.MyVirtualFileHelper;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

public class VirtualBlenderFile {

    public final MyProjectHolder project;
    private final BlenderSettings settings;

    private final VirtualFile selectedVirtualFile;
    private final VirtualFile rootVirtualFile;

    public final boolean isRoot;

    public VirtualBlenderFile(MyProjectHolder project, VirtualFile virtualFile) {
        this.project = project;
        this.selectedVirtualFile = virtualFile;

        VirtualFile parent = virtualFile.getParent();

        this.isRoot = parent != null && parent.getPath().equals(project.getBasePath());
        this.rootVirtualFile = this.isRoot ? virtualFile : MyVirtualFileHelper.getProjectFirstVirtualFile(project, virtualFile);
        this.settings = BlenderSettings.getBlenderSettings(project);
    }

    /*
     *  Data
     */

    public boolean isRootAndBlenderAddon() {
        return isRoot && settings.isBlenderAddon(rootVirtualFile.getName());
    }

    public String getRelativeAddonName() {
        return settings.isBlenderAddon(rootVirtualFile.getName()) ? rootVirtualFile.getName() : null;
    }

    public void markAsAddonFolder() {
        if (isRootAndBlenderAddon()) return;

        settings.addBlenderAddon(rootVirtualFile.getName());
        sync();
    }

    public void unmarkAsAddonFolder() {
        if (!isRootAndBlenderAddon()) return;

        settings.removeBlenderAddon(rootVirtualFile.getName());
        sync();
    }

    private void sync() {
        project.getProject().save();
    }

    public VirtualFile getRootVirtualFile() {
        return rootVirtualFile;
    }

    public boolean isSource() {
        for(VirtualFile f : ProjectRootManager.getInstance(project.getProject()).getContentSourceRoots()){
            if(f.equals(selectedVirtualFile))
                return true;
        }
        return false;
    }
}
