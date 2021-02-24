package ui.dialogs.add_blender_instance;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AddBlenderInstanceWrapper extends DialogWrapper {

    private final Project project;
    public AddBlenderInstance form;

    public AddBlenderInstanceWrapper(@NotNull Project project) {
        super(true);
        this.project = project;
        init();
        setTitle("Add Blender Instance");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new AddBlenderInstance(project);
        return form.getJComponent();
    }
}
