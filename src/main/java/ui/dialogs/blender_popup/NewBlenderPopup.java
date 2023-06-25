package ui.dialogs.blender_popup;

import com.intellij.ui.components.JBLabel;

import javax.swing.*;

public class NewBlenderPopup extends JDialog {

    private JPanel contentPane;
    private JBLabel text;
    private JBLabel subText;

    public NewBlenderPopup(String text, String subText) {
        setContentPane(contentPane);
        this.text.setText(text);
        this.subText.setText(subText);
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
