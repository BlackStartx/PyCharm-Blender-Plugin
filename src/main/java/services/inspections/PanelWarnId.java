package services.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.jetbrains.python.psi.*;
import org.jetbrains.annotations.NotNull;
import services.inspections.base.OperatorIdInspection;

import java.util.regex.Pattern;

public class PanelWarnId extends OperatorIdInspection {
    private static final int limit = 2;
    private static final String div = "_PT_";
    private static final String regex = "(?i)" + div;
    private static final Pattern pattern = Pattern.compile(regex);
    private static final String description = "Blender: Id should be lowercase in order to be registered in to Blender.";

    @Override
    public String correctClass() {
        return "bpy.types.Panel";
    }

    @Override
    protected void visitor(@NotNull ProblemsHolder holder, @NotNull PyAssignmentStatement node) {
        for (PyExpression expression : node.getTargets()) {
            PyExpression expressionValue = getPyExpression(node, expression);
            if (expressionValue == null) continue;

            String text = expressionValue.getText();
            String[] divided = pattern.split(text, limit);
            String prefix = divided.length < 1 ? null : divided[0];
            String suffix = divided.length < 2 ? null : divided[1];
            if (finalCheck(text, prefix, suffix)) continue;

            holder.registerProblem(expressionValue, description, new BlIdPrefixSuFix(prefix, suffix));
        }
    }

    private boolean finalCheck(String text, String prefix, String suffix) {
        if (prefix == null || suffix == null) return false;
        if (prefix.equals("\"") || suffix.equals("\"")) return false;
        return text.equals(prefix.toUpperCase() + div + suffix);
    }

    private static class BlIdPrefixSuFix implements LocalQuickFix {
        private final String s = "\"";
        private final String prefix;
        private final String suffix;
        private final boolean prefixNullOrEmpty;
        private final boolean suffixNullOrEmpty;

        public BlIdPrefixSuFix(String prefix, String suffix) {
            this.prefix = clean(prefix);
            this.suffix = clean(suffix);
            this.prefixNullOrEmpty = this.prefix == null || this.prefix.isEmpty();
            this.suffixNullOrEmpty = this.suffix == null || this.suffix.isEmpty();
        }

        protected String clean(String code) {
            return code == null ? null : code.equals(s) ? "" : code.substring(code.startsWith(s) ? 1 : 0, code.endsWith(s) ? code.length() - 1 : code.length());
        }

        @Override
        public @IntentionName @NotNull String getName() {
            return suffix == null ? ("Create '" + div + "' suffix") : "Fix prefix and suffix case";
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PyExpression binaryExpression = (PyExpression) descriptor.getPsiElement();
            String newValue = (prefixNullOrEmpty ? "prefix" : prefix).toUpperCase() + div + (suffixNullOrEmpty ? "suffix" : suffix);
            newValue = s + newValue + s;
            PyExpression expression = PyElementGenerator.getInstance(project).createExpressionFromText(LanguageLevel.getDefault(), newValue);
            binaryExpression.replace(expression);
        }
    }
}
