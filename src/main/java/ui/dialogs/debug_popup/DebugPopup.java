package ui.dialogs.debug_popup;

import javax.swing.*;

public class DebugPopup extends JDialog {

    private JPanel contentPane;

    public DebugPopup() {
        setContentPane(contentPane);
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
