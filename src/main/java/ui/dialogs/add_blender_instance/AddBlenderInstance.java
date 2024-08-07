package ui.dialogs.add_blender_instance;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import data.BlenderExeFileChooserDescriptor;
import data.BlenderInstance;
import org.jetbrains.annotations.NotNull;
import util.MySwingUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

public class AddBlenderInstance extends JDialog {
    private final HashMap<String, String> env = new HashMap<>();
    private final EnvModel model;
    private JPanel contentPane;
    private JPanel extraPanel;
    private JBTextField path;
    private JBTextField name;
    private JBLabel explore;
    private JBTable environment;
    private JBLabel add;
    private JBLabel arrow;
    private boolean extraSettings = false;

    AddBlenderInstance(@NotNull Project project, BlenderInstance from) {
        setContentPane(contentPane);
        initIcons();
        MySwingUtil.setLabelOnClickListener(explore, () -> onExploreClick(project));
        MySwingUtil.setLabelOnClickListener(arrow, this::onExtraSettings);
        MySwingUtil.setLabelOnClickListener(add, this::addNewEnv);
        environment.setModel(model = new EnvModel());
        if (from != null) init(from);
    }

    private void initIcons() {
        this.explore.setIcon(AllIcons.Nodes.Folder);
        this.arrow.setIcon(AllIcons.General.ArrowRight);
        this.add.setIcon(AllIcons.General.Add);
    }

    private void init(BlenderInstance from) {
        name.setText(from.name);
        path.setText(from.path);
        if (from.environment != null) env.putAll(from.environment);
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
        return new BlenderInstance(path.getText(), name.getText(), env);
    }

    private void addNewEnv() {
        env.put("<new key>", "");
        model.fireTableDataChanged();
    }

    private void onExtraSettings() {
        extraSettings = !extraSettings;
        this.arrow.setIcon(extraSettings ? AllIcons.General.ArrowDown : AllIcons.General.ArrowRight);
        this.extraPanel.setVisible(extraSettings);
    }

    public void updateConfiguration(BlenderInstance update) {
        update.path = path.getText();
        update.name = name.getText();
        update.environment = env;
    }

    private class EnvModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return env.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnIndex == 0 ? env.keySet().toArray()[rowIndex] : env.values().toArray()[rowIndex];
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            String key = (String) env.keySet().toArray()[rowIndex];
            String val = (String) env.values().toArray()[rowIndex];

            if (columnIndex == 0) {
                env.remove(key);
                key = (String) value;
                if (!key.isEmpty()) env.put(key, val);
            } else env.put(key, (String) value);

            fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public String getColumnName(int column) {
            return column == 0 ? "Key" : "Value";
        }
    }
}
