package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import data.VirtualBlenderFile;
import icons.BlendCharmIcons;
import org.jetbrains.annotations.NotNull;
import util.MyProjectHolder;

public class MarkAsAddonProjectAction extends AnAction {

    private final MyProjectHolder project;

    MarkAsAddonProjectAction(MyProjectHolder project) {
        super("Mark as Addon Project", "Mark the project as a Plugin Folder", BlendCharmIcons.BLENDER_FOLDER_ICON);
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        VirtualFile[] virtualFiles = anActionEvent.getDataContext().getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);

        if (virtualFiles == null || virtualFiles.length == 0) return;
        new VirtualBlenderFile(project, virtualFiles[0]).markAsAddonProject();
    }
}
