package actions;

import icons.BlendCharmIcons;
import data.VirtualBlenderFile;
import util.MyProjectHolder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class MarkAsAddonAction extends AnAction {

    private final MyProjectHolder project;

    MarkAsAddonAction(MyProjectHolder project) {
        super("Mark as Addon Folder", "Mark the selected folder as a Plugin Folder", BlendCharmIcons.BLENDER_FOLDER_ICON);
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        VirtualFile[] virtualFiles = anActionEvent.getDataContext().getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);

        if (virtualFiles == null) return;

        for (VirtualFile file : virtualFiles) {
            VirtualBlenderFile virtualBlenderFile = new VirtualBlenderFile(project, file);
            virtualBlenderFile.markAsAddonFolder();
        }
    }
}
