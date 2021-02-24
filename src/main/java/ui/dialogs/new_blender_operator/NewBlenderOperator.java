package ui.dialogs.new_blender_operator;

import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class NewBlenderOperator extends JDialog {

    private final Project project;

    private JPanel contentPane;
    private JTextField label;
    private JTextField idName;

    public NewBlenderOperator(@NotNull Project project) {
        this.project = project;
        setContentPane(contentPane);
    }

    private String getLabelForCode() {
        return label.getText().replaceAll("[^A-Za-z]+", "");
    }

    public String getOperatorClassName() {
        return getLabelForCode() + "Operator";
    }

    public String getLabel() {
        return label.getText();
    }

    public String getIdName() {
        return idName.getText();
    }

    public String getOperatorFileName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getLabelForCode()) + ".py";
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
