package ui.dialogs.add_blender_instance;

import com.intellij.icons.AllIcons;
import data.BlenderExeFileChooserDescriptor;
import data.BlenderInstance;
import util.MySwingUtil;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;

public class AddBlenderInstance extends JDialog {

    private JPanel contentPane;
    private JTextField path;
    private JTextField name;
    private JLabel explore;

    AddBlenderInstance(@NotNull Project project, BlenderInstance from) {
        setContentPane(contentPane);
        initIcons();
        MySwingUtil.setLabelOnClickListener(explore, () -> onExploreClick(project));
        if (from == null) return;
        name.setText(from.name);
        path.setText(from.path);
    }

    private void initIcons() {
        this.explore.setIcon(AllIcons.Nodes.Folder);
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

    public BlenderInstance getNewConfiguration() {
        return new BlenderInstance(path.getText(), name.getText(), new HashMap<>());
    }

    public void updateConfiguration(BlenderInstance update) {
        update.path = path.getText();
        update.name = name.getText();
    }
}
