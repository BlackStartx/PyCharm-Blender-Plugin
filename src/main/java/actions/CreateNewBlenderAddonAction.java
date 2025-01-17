package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import data.VirtualBlenderFile;
import icons.BlendCharmIcons;
import org.jetbrains.annotations.NotNull;
import ui.dialogs.new_blender_addon.NewBlenderAddonWrapper;
import util.MyInputStreamHelper;
import util.MyProjectHolder;
import util.core.MyFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateNewBlenderAddonAction extends AnAction {

    private final MyProjectHolder project;

    CreateNewBlenderAddonAction(MyProjectHolder project) {
        super("Create New Blender Addon", "Creates a new Blender Addon", BlendCharmIcons.BLENDER_LOGO);
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        NewBlenderAddonWrapper dialog = new NewBlenderAddonWrapper(project.getProject());
        if (dialog.showAndGet()) {
            String projectPath = project.getBasePath();
            if (projectPath == null) return;

            try {
                if (!dialog.form.isNameValid()) return;

                String directoryPath = Paths.get(projectPath, dialog.form.getBlenderAddonFolderName()).toString();
                VirtualFile directory = VfsUtil.createDirectories(directoryPath);

                Module module = ModuleUtil.findModuleForFile(directory, project.getProject());
                if (module == null) return;

                ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
                rootModel.getContentEntries()[0].addSourceFolder(directory, false);

                VirtualBlenderFile virtualBlenderFile = new VirtualBlenderFile(project, directory);
                virtualBlenderFile.markAsAddonFolder();

                String stream = MyInputStreamHelper.readString(this.getClass().getClassLoader().getResourceAsStream("Python/Templates/new_addon.py"));
                stream = stream.replace("$ADDON_NAME$", dialog.form.getBlenderAddonName());
                stream = stream.replace("$ADDON_AUTHOR$", dialog.form.getBlenderAddonAuthor());
                stream = stream.replace("$ADDON_DESCRIPTION$", dialog.form.getBlenderAddonDescription());

                MyFileUtils.write(Path.of(Paths.get(directoryPath, "__init__.py").toString()), stream);
                LocalFileSystemImpl.getInstance().refresh(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
