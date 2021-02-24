package actions;

import data.VirtualBlenderFile;
import util.MyProjectHolder;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DynamicActionGroup extends ActionGroup {

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        if (anActionEvent == null) return new AnAction[0];

        MyProjectHolder project = new MyProjectHolder(anActionEvent.getProject());
        VirtualFile virtualFile = anActionEvent.getDataContext().getData(PlatformDataKeys.VIRTUAL_FILE);

        if (virtualFile == null) return new AnAction[0];

        VirtualBlenderFile virtualBlenderFile = new VirtualBlenderFile(project, virtualFile);
        ArrayList<AnAction> actions = new ArrayList<>();

        if (virtualBlenderFile.isRootAndBlenderAddon()) {
            actions.add(new CreateNewBlenderPanelAction(project, virtualBlenderFile));
            actions.add(new CreateNewBlenderOperatorAction(project, virtualBlenderFile));
            actions.add(new Separator());
            actions.add(new UnmarkAsAddonAction(project));
        } else {
            actions.add(new CreateNewBlenderAddonAction(project));
            if (virtualBlenderFile.isRoot) {
                actions.add(new Separator());
                actions.add(new MarkAsAddonAction(project));
            }
        }
        return actions.toArray(new AnAction[0]);
    }
}
