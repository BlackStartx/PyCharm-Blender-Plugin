package util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import settings.BlenderSettings;

import java.net.MalformedURLException;
import java.net.URL;

public class MyProjectHolder {

    public final VirtualFile projectVirtualFile;
    private final Project project;

    public MyProjectHolder(Project project) {
        this.project = project;
        this.projectVirtualFile = getProjectVirtualFile(project);
    }

    private static VirtualFile getProjectVirtualFile(Project project) {
        try {
            return VfsUtil.findFileByURL(new URL("file:\\" + project.getBasePath()));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String addonContainerPath() {
        return settings().isBlenderProject() ? projectVirtualFile.getParent().getPath() : getBasePath();
    }

    public String getBasePath() {
        return project.getBasePath();
    }

    public Project getProject() {
        return project;
    }

    public BlenderSettings settings() {
        return BlenderSettings.getBlenderSettings(this);
    }
}
