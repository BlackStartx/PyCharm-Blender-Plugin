package ui.dialogs.blender_popup;

import javax.swing.*;

public class NewBlenderPopup extends JDialog {

    private JPanel contentPane;
    private JLabel text;
    private JLabel subText;

    public NewBlenderPopup(String text, String subText) {
        setContentPane(contentPane);
        this.text.setText(text);
        this.subText.setText(subText);
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
