package ui.dialogs.remove_blender_instance;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RemoveBlenderInstanceWrapper extends DialogWrapper {

    public RemoveBlenderInstance form;

    public RemoveBlenderInstanceWrapper() {
        super(true);
        init();
        setTitle("Remove Blender Instance");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new RemoveBlenderInstance();
        return form.getJComponent();
    }
}
