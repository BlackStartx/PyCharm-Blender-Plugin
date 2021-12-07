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
        VirtualFile[] virtualFiles = anActionEvent.getDataContext().getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);

        if (virtualFiles == null || virtualFiles.length == 0) return new AnAction[0];
        boolean root = true;
        boolean addon = true;
        boolean subRoot = true;

        for(VirtualFile file : virtualFiles){
            VirtualBlenderFile blenderFile = new VirtualBlenderFile(project, file);
            if(!blenderFile.isRoot) root = false;
            if(!blenderFile.isSubRoot) subRoot = false;
            if(!blenderFile.isBlenderAddon()) addon = false;
        }

        ArrayList<AnAction> actions = new ArrayList<>();
        if (addon && virtualFiles.length == 1) {
            VirtualBlenderFile virtualBlenderFile = new VirtualBlenderFile(project, virtualFiles[0]);
            actions.add(new CreateNewBlenderPanelAction(project, virtualBlenderFile));
            actions.add(new CreateNewBlenderOperatorAction(project, virtualBlenderFile));
            actions.add(new Separator());
            if(subRoot) actions.add(new UnmarkAsAddonAction(project));
            if(root) actions.add(new UnmarkAsAddonProjectAction(project));
        } else {
            actions.add(new CreateNewBlenderAddonAction(project));
            if (subRoot && !project.settings().isBlenderProject()) {
                actions.add(new Separator());
                actions.add(new MarkAsAddonAction(project));
            }
            if (root) {
                actions.add(new Separator());
                actions.add(new MarkAsAddonProjectAction(project));
            }
        }
        return actions.toArray(new AnAction[0]);
    }
}
