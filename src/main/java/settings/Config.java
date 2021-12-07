package settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Config implements Configurable {

    private final Project project;
    private final BlenderSettings settings;
    private final JBCheckBox checkBox;

    public Config(Project project) {
        this.project = project;
        this.settings = BlenderSettings.getBlenderSettings(project);
        this.checkBox = new JBCheckBox("Enable verbose:", settings.data.showVerbose);
    }

    @Override
    public String getDisplayName() {
        return "Blend-Charm Settings";
    }

    @Override
    public @Nullable
    JComponent createComponent() {
        return FormBuilder.createFormBuilder()
                .addComponent(checkBox, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    @Override
    public boolean isModified() {
        return settings.data.showVerbose != checkBox.isSelected();
    }

    @Override
    public void reset() {
        checkBox.setSelected(settings.data.showVerbose);
    }

    @Override
    public void apply() {
        settings.data.showVerbose = checkBox.isSelected();
        project.save();
    }
}
