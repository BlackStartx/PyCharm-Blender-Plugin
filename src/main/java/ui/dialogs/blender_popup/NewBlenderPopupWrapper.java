package ui.dialogs.blender_popup;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NewBlenderPopupWrapper extends DialogWrapper {

    private final String text;
    private final String subText;

    public NewBlenderPopup form;

    public static void show(String title, String text, String subText) {
        new NewBlenderPopupWrapper(title, text, subText).show();
    }

    public NewBlenderPopupWrapper(String title, String text, String subText) {
        super(true);
        this.text = text;
        this.subText = subText;
        init();
        setTitle(title);
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{this.getOKAction()};
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        form = new NewBlenderPopup(text, subText);
        return form.getJComponent();
    }
}
