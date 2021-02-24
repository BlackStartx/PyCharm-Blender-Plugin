package data;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;

public class RunningBlenderProcessRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        RunningBlenderProcess blenderProcess = (RunningBlenderProcess) value;
        setText(blenderProcess.getProcessName());
        setIcon(blenderProcess.isDebug() ? AllIcons.Actions.StartDebugger : AllIcons.Actions.Execute);

        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        setEnabled(true);
        setFont(list.getFont());

        return this;
    }
}
