package actions;

import data.VirtualBlenderFile;
import util.core.MyFileUtils;
import util.MyInputStreamHelper;
import util.MyProjectHolder;
import ui.dialogs.new_blender_operator.NewBlenderOperatorWrapper;
import com.intellij.history.core.Paths;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import org.jetbrains.annotations.NotNull;

public class CreateNewBlenderOperatorAction extends AnAction {

    private final MyProjectHolder project;
    private final VirtualBlenderFile virtualBlenderFile;

    CreateNewBlenderOperatorAction(MyProjectHolder project, VirtualBlenderFile virtualBlenderFile) {
        super("Create Operator");
        this.virtualBlenderFile = virtualBlenderFile;
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        NewBlenderOperatorWrapper dialog = new NewBlenderOperatorWrapper(project.getProject());
        if (dialog.showAndGet()) {
            String panelsPath = Paths.appended(virtualBlenderFile.getRootVirtualFile().getPath(), "operators");

            String stream = MyInputStreamHelper.readString(this.getClass().getClassLoader().getResourceAsStream("Python/Templates/new_operator.py"));
            stream = stream.replace("OPERATOR_CLASS_NAME", dialog.form.getOperatorClassName());
            stream = stream.replace("$ID_NAME$", dialog.form.getIdName());
            stream = stream.replace("$LABEL$", dialog.form.getLabel());

            MyFileUtils.createDirIfNotExist(panelsPath);
            MyFileUtils.writeText(Paths.appended(panelsPath, dialog.form.getOperatorFileName()), stream);

            LocalFileSystemImpl.getInstance().refresh(true);
        }
    }
}
