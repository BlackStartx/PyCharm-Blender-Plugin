package ui.dialogs.remove_blender_instance;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RemoveBlenderInstanceWrapper extends DialogWrapper {

    private final Project project;
    public RemoveBlenderInstance form;

    public RemoveBlenderInstanceWrapper(@NotNull Project project) {
        super(true);
        this.project = project;
        init();
        setTitle("Remove Blender Instance");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new RemoveBlenderInstance(project);
        return form.getJComponent();
    }
}
