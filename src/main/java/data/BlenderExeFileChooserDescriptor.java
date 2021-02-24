package data;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileElement;
import com.intellij.openapi.vfs.VirtualFile;

public class BlenderExeFileChooserDescriptor extends FileChooserDescriptor {
    public BlenderExeFileChooserDescriptor() {
        super(true, false, false, false, false, false);
    }

    @Override
    public boolean isFileSelectable(VirtualFile file) {
        String extension = file.getExtension();
        return extension != null && extension.toLowerCase().equals("exe");
    }

    @Override
    public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
        if (FileElement.isFileHidden(file) && !showHiddenFiles) return false;
        if (file.isDirectory()) return true;

        String extension = file.getExtension();
        return extension != null && extension.toLowerCase().equals("exe");
    }
}
