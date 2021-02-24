package ui.dialogs.remove_blender_instance;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RemoveBlenderInstance extends JDialog {

    private final Project project;

    private JPanel contentPane;

    public RemoveBlenderInstance(@NotNull Project project) {
        this.project = project;
        setContentPane(contentPane);
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
