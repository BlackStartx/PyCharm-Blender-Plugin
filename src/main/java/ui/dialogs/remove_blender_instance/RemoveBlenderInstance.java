package ui.dialogs.remove_blender_instance;

import javax.swing.*;

public class RemoveBlenderInstance extends JDialog {

    private JPanel contentPane;

    public RemoveBlenderInstance() {
        setContentPane(contentPane);
    }

    JComponent getJComponent() {
        return contentPane;
    }
}
