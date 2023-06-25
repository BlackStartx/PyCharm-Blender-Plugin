package util;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MySwingUtil {

    public static void setLabelOnClickListener(JBLabel to, Runnable method) {
        to.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                method.run();
            }
        });
    }

    public static void setFieldTextChangeListener(JBTextField addon_name, Runnable onNameChange) {
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
