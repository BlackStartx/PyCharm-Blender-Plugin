package ui.dialogs.debug_popup;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DebugPopupWrapper extends DialogWrapper {

    public DebugPopup form;

    public DebugPopupWrapper() {
        super(true);
        setTitle("New Debug Information");
        init();
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{this.getOKAction()};
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new DebugPopup();
        return form.getJComponent();
    }
}
