package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import data.VirtualBlenderFile;
import org.jetbrains.annotations.NotNull;
import ui.dialogs.new_blender_panel.NewBlenderPanelWrapper;
import util.MyInputStreamHelper;
import util.MyProjectHolder;
import util.core.MyFileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateNewBlenderPanelAction extends AnAction {

    private final MyProjectHolder project;
    private final VirtualBlenderFile virtualBlenderFile;

    CreateNewBlenderPanelAction(MyProjectHolder project, VirtualBlenderFile virtualBlenderFile) {
        super("Create Panel");
        this.virtualBlenderFile = virtualBlenderFile;
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        NewBlenderPanelWrapper dialog = new NewBlenderPanelWrapper(project.getProject());
        if (dialog.showAndGet()) {
            String panelsPath = Paths.get(virtualBlenderFile.getSubRootVirtualFile().getPath(), "panels").toString();

            String stream = MyInputStreamHelper.readString(this.getClass().getClassLoader().getResourceAsStream("Python/Templates/new_panel.py"));
            stream = stream.replace("PANEL_CLASS_NAME", dialog.form.getPanelClassName());
            stream = stream.replace("$ID_NAME$", dialog.form.getPanelIdName());
            stream = stream.replace("$LABEL$", dialog.form.getLabel());
            stream = stream.replace("$CATEGORY$", dialog.form.getCategory());
            stream = stream.replace("$SPACE_TYPE$", dialog.form.getSpaceType());
            stream = stream.replace("$REGION_TYPE$", dialog.form.getRegionType());

            if (MyFileUtils.cantCreateDirectory(panelsPath)) return;
            MyFileUtils.write(Path.of(Paths.get(panelsPath, dialog.form.getPanelFileName()).toString()), stream);
            LocalFileSystemImpl.getInstance().refresh(true);
        }
    }
}
