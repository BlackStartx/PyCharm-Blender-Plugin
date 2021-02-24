package ui.dialogs.new_blender_addon;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NewBlenderAddonWrapper extends DialogWrapper {

    private final Project project;
    public NewBlenderAddon form;

    public NewBlenderAddonWrapper(@NotNull Project project) {
        super(true);
        this.project = project;
        init();
        setTitle("Create New Blender Addon");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new NewBlenderAddon(project, myOKAction);
        return form.getJComponent();
    }
}
