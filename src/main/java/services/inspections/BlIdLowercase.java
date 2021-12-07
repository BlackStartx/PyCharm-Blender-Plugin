package services.inspections;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.python.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlIdLowercase extends LocalInspectionTool {

    private static final String value = "bl_id" + "name";
    private static final String description = "Blender: Id should be lowercase in order to be registered in to Blender.";

    @Override
    public @NotNull
    PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new PyElementVisitor() {
            @Override
            public void visitPyAssignmentStatement(@NotNull PyAssignmentStatement node) {
                for (PyExpression expression : node.getTargets()) {
                    if (!Objects.equals(expression.getName(), value)) continue;

                    PyExpression expressionValue = node.getAssignedValue();
                    if (expressionValue == null) continue;

                    String text = expressionValue.getText();
                    if (text.equals(text.toLowerCase())) continue;

                    holder.registerProblem(expressionValue, description, new BlIdLowercaseFix());
                }
            }
        };
    }

    private static class BlIdLowercaseFix implements LocalQuickFix {

        @Override
        public @IntentionName
        @NotNull
        String getName() {
            return "Convert to lowercase";
        }

        @Override
        public @IntentionFamilyName
        @NotNull
        String getFamilyName() {
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
