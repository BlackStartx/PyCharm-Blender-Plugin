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

public class OperatorErrId extends OperatorIdInspection {
    private static final String description = "Blender: Id should be lowercase in order to be registered in to Blender.";

    @Override
    public String correctClass() {
        return "bpy.types.Operator";
    }

    @Override
    protected void visitor(@NotNull ProblemsHolder holder, @NotNull PyAssignmentStatement node) {
        for (PyExpression expression : node.getTargets()) {
            PyExpression expressionValue = getPyExpression(node, expression);
            if (expressionValue == null) continue;

            String text = expressionValue.getText();
            if (text.equals(text.toLowerCase())) continue;

            holder.registerProblem(expressionValue, description, new OperatorErrId.BlIdLowercaseFix());
        }
    }

    private static class BlIdLowercaseFix implements LocalQuickFix {

        @Override
        public @IntentionName @NotNull String getName() {
            return "Convert to lowercase";
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PyExpression binaryExpression = (PyExpression) descriptor.getPsiElement();

            PyExpression expression = PyElementGenerator.getInstance(project).createExpressionFromText(LanguageLevel.getDefault(), binaryExpression.getText().toLowerCase());
            binaryExpression.replace(expression);
        }
    }
}
