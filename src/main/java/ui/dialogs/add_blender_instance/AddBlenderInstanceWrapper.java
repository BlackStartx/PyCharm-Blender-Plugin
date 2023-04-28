package ui.dialogs.add_blender_instance;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import data.BlenderInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AddBlenderInstanceWrapper extends DialogWrapper {

    private final Project project;
    public final BlenderInstance from;
    public AddBlenderInstance form;

    public AddBlenderInstanceWrapper(@NotNull Project project, BlenderInstance from) {
        super(true);
        this.project = project;
        this.from = from;
        init();
        setTitle((from == null ? "Add" : "Update") + " Blender Instance");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new AddBlenderInstance(project, from);
        return form.getJComponent();
    }
}
