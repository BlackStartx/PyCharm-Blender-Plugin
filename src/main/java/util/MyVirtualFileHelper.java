package util;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.Objects;

public class MyVirtualFileHelper {
    public static VirtualFile getProjectFirstVirtualFile(MyProjectHolder project, VirtualFile virtualFile) {
        try {
            return project.projectVirtualFile.findChild(virtualFile.getPath().substring(Objects.requireNonNull(project.getBasePath()).length()).split("/", 1)[0].split("/", 3)[1]);
        } catch (Exception e) {
            return null;
        }
    }
}
