package ui.dialogs.new_blender_panel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NewBlenderPanelWrapper extends DialogWrapper {

    private final Project project;
    public NewBlenderPanel form;

    public NewBlenderPanelWrapper(@NotNull Project project) {
        super(true);
        this.project = project;
        init();
        setTitle("Create New Blender Panel");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new NewBlenderPanel(project);
        return form.getJComponent();
    }
}
