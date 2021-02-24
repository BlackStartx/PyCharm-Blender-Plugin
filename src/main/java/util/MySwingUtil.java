package util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MySwingUtil {

    public static void setLabelOnClickListener(JLabel to, Runnable method) {
        to.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                method.run();
            }
        });
    }

    public static void setFieldTextChangeListener(JTextField addon_name, Runnable onNameChange) {
        addon_name.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onNameChange.run();
            }

            public void removeUpdate(DocumentEvent e) {
                onNameChange.run();
            }

            public void insertUpdate(DocumentEvent e) {
                onNameChange.run();
            }
        });
    }
}
