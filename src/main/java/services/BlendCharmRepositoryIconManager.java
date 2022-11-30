package services;

import icons.BlendCharmIcons;
import data.VirtualBlenderFile;
import util.MyProjectHolder;
import com.intellij.ide.IconProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.file.PsiDirectoryImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BlendCharmRepositoryIconManager extends IconProvider {
    @Nullable
    @Override
    public Icon getIcon(@NotNull PsiElement psiElement, int i) {
        if (!(psiElement instanceof PsiDirectoryImpl c)) return null;

        VirtualFile virtualFile = c.getVirtualFile();
        MyProjectHolder project = new MyProjectHolder(psiElement.getProject());

        VirtualBlenderFile virtualBlenderFile = new VirtualBlenderFile(project, virtualFile);
        boolean addon = virtualBlenderFile.isSubRootAndBlenderAddon() || virtualBlenderFile.isRootAndBlenderProject();

        return !addon ? null : virtualBlenderFile.isSource() ? BlendCharmIcons.BLENDER_SRC_FOLDER_ICON : BlendCharmIcons.BLENDER_FOLDER_ICON;
    }
}
