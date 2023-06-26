package actions;

import com.intellij.history.core.Paths;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import data.VirtualBlenderFile;
import org.jetbrains.annotations.NotNull;
import ui.dialogs.new_blender_operator.NewBlenderOperatorWrapper;
import util.MyInputStreamHelper;
import util.MyProjectHolder;
import util.core.MyFileUtils;

import java.nio.file.Path;

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
            String panelsPath = Paths.appended(virtualBlenderFile.getSubRootVirtualFile().getPath(), "operators");

            String stream = MyInputStreamHelper.readString(this.getClass().getClassLoader().getResourceAsStream("Python/Templates/new_operator.py"));
            stream = stream.replace("OPERATOR_CLASS_NAME", dialog.form.getOperatorClassName());
            stream = stream.replace("$ID_NAME$", dialog.form.getIdName());
            stream = stream.replace("$LABEL$", dialog.form.getLabel());

            if (MyFileUtils.cantCreateDirectory(panelsPath)) return;
            MyFileUtils.write(Path.of(Paths.appended(panelsPath, dialog.form.getOperatorFileName())), stream);
            LocalFileSystemImpl.getInstance().refresh(true);
        }
    }
}
