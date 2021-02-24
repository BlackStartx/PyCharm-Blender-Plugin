package ui.dialogs.add_blender_instance;

import data.BlenderExeFileChooserDescriptor;
import data.BlenderInstance;
import util.MySwingUtil;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AddBlenderInstance extends JDialog {

    private final Project project;

    private JPanel contentPane;
    private JTextField path;
    private JTextField name;
    private JLabel explore;

    AddBlenderInstance(@NotNull Project project) {
        this.project = project;
        setContentPane(contentPane);
        MySwingUtil.setLabelOnClickListener(explore, () -> onExploreClick(project));
    }

    private void onExploreClick(@NotNull Project project) {
        FileChooserDescriptor descriptor = new BlenderExeFileChooserDescriptor();
        descriptor.setTitle("Blender Runnable.");
        descriptor.setDescription("Select Blender runnable file.");
        VirtualFile[] files = FileChooser.chooseFiles(descriptor, project, null);
        if (files.length == 0) return;

        path.setText(files[0].getPath());
    }

    JComponent getJComponent() {
        return contentPane;
    }

    public BlenderInstance getConfiguration() {
        return new BlenderInstance(path.getText(), name.getText());
    }
}
