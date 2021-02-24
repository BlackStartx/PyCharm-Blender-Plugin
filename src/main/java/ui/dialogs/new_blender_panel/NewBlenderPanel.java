package ui.dialogs.new_blender_panel;

import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class NewBlenderPanel extends JDialog {

    private final Project project;

    private JPanel contentPane;
    private JTextField label;
    private JTextField category;
    private JComboBox<String> regions;
    private JComboBox<String> spaces;

    public NewBlenderPanel(@NotNull Project project) {
        this.project = project;
        setContentPane(contentPane);

        setRegions();
        setSpaces();
    }

    private String getLabelForCode() {
        return label.getText().replaceAll("[^A-Za-z]+", "");
    }

    public String getPanelClassName() {
        return getLabelForCode() + "Panel";
    }

    public String getPanelIdName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, getLabelForCode()) + "_PT_Panel";
    }

    public String getPanelFileName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getLabelForCode()) + ".py";
    }

    public String getLabel() {
        return label.getText();
    }

    public String getCategory() {
        return category.getText();
    }

    public String getSpaceType() {
        return (String) spaces.getSelectedItem();
    }

    public String getRegionType() {
        return (String) regions.getSelectedItem();
    }

    private void setSpaces() {
        spaces.addItem("EMPTY");
        spaces.addItem("VIEW_3D");
        spaces.addItem("IMAGE_EDITOR");
        spaces.addItem("NODE_EDITOR");
        spaces.addItem("SEQUENCE_EDITOR");
        spaces.addItem("CLIP_EDITOR");
        spaces.addItem("DOPESHEET_EDITOR");
        spaces.addItem("GRAPH_EDITOR");
        spaces.addItem("NLA_EDITOR");
        spaces.addItem("TEXT_EDITOR");
        spaces.addItem("CONSOLE");
        spaces.addItem("INFO");
        spaces.addItem("TOPBAR");
        spaces.addItem("STATUSBAR");
        spaces.addItem("OUTLINER");
        spaces.addItem("PROPERTIES");
        spaces.addItem("FILE_BROWSER");
        spaces.addItem("PREFERENCES");
    }

    private void setRegions() {
        regions.addItem("WINDOW");
        regions.addItem("HEADER");
        regions.addItem("CHANNELS");
        regions.addItem("TEMPORARY");
        regions.addItem("UI");
        regions.addItem("TOOLS");
        regions.addItem("TOOL_PROPS");
        regions.addItem("PREVIEW");
        regions.addItem("HUD");
        regions.addItem("NAVIGATION_BAR");
        regions.addItem("EXECUTE");
        regions.addItem("FOOTER");
        regions.addItem("TOOL_HEADER");
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
