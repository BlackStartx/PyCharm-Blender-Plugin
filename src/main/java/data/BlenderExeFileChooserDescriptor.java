package data;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileElement;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Set;

public class BlenderExeFileChooserDescriptor extends FileChooserDescriptor {
    private static final Set<String> extensions = Set.of("exe", "app");

    public BlenderExeFileChooserDescriptor() {
        super(true, false, false, false, false, false);
    }

    @Override
    public boolean isFileSelectable(VirtualFile file) {
        if (file == null) return false;
        String extension = file.getExtension();
        return extension != null && extensions.contains(extension.toLowerCase());
    }

    @Override
    public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
        if (FileElement.isFileHidden(file) && !showHiddenFiles) return false;
        if (file.isDirectory()) return true;

        String extension = file.getExtension();
        return extension != null && extensions.contains(extension.toLowerCase());
    }
}
