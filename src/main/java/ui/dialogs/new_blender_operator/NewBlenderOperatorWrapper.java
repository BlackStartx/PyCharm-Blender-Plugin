package ui.dialogs.new_blender_operator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NewBlenderOperatorWrapper extends DialogWrapper {

    private final Project project;
    public NewBlenderOperator form;

    public NewBlenderOperatorWrapper(@NotNull Project project) {
        super(true);
        this.project = project;
        init();
        setTitle("Create New Blender Operator");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new NewBlenderOperator(project);
        return form.getJComponent();
    }
}
