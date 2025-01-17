package ui.dialogs.new_blender_addon;

import com.intellij.ui.components.JBTextField;
import util.MySwingUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class NewBlenderAddon extends JDialog {

    private final Action okAction;

    private JPanel contentPane;
    private JBTextField addon_name;
    private JBTextField addon_author;
    private JBTextField addon_description;

    public NewBlenderAddon(@NotNull Project project, @NotNull Action okAction) {
        this.okAction = okAction;
        this.okAction.setEnabled(false);

        MySwingUtil.setFieldTextChangeListener(addon_name, this::onNameChange);

        setContentPane(contentPane);
    }

    private void onNameChange() {
        this.okAction.setEnabled(isNameValid());
    }

    public String getBlenderAddonFolderName() {
        return "Addon - " + addon_name.getText();
    }

    public String getBlenderAddonName() {
        return addon_name.getText();
    }

    public String getBlenderAddonAuthor() {
        return addon_author.getText();
    }

    public String getBlenderAddonDescription() {
        return addon_description.getText();
    }

    JComponent getJComponent() {
        return contentPane;
    }

    public boolean isNameValid() {
        if (addon_name.getText().isEmpty()) return false;

        try {
            Paths.get(addon_name.getText());
        } catch (InvalidPathException ex) {
            return false;
        }
        return true;
    }
}
