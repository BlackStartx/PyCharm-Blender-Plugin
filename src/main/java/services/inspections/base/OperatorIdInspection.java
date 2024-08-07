package services.inspections.base;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.python.psi.PyAssignmentStatement;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyElementVisitor;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl;
import com.jetbrains.python.psi.impl.PyTargetExpressionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public abstract class OperatorIdInspection extends LocalInspectionTool {
    protected static final String value = "bl_idname";
    protected static final String separator = ".";

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new PyElementVisitor() {
            @Override
            public void visitPyAssignmentStatement(@NotNull PyAssignmentStatement node) {
                visitor(holder, node);
            }
        };
    }

    @Nullable
    protected PyExpression getPyExpression(@NotNull PyAssignmentStatement node, PyExpression expression) {
        if (!Objects.equals(expression.getName(), value)) return null;

        PyExpression expressionValue = node.getAssignedValue();
        if (expressionValue == null) return null;

        PyTargetExpressionImpl targetExpression = (PyTargetExpressionImpl) expression;
        PyClass targetClass = targetExpression.getContainingClass();
        if (targetClass == null) return null;

        PyExpression[] superClasses = targetClass.getSuperClassExpressions();
        if (Arrays.stream(superClasses).noneMatch(this::isClass)) return null;
        return expressionValue;
    }

    protected boolean isClass(PyExpression pyExpression) {
        if (pyExpression == null) return false;
        PyReferenceExpressionImpl expression = (PyReferenceExpressionImpl) pyExpression;

        var qualifiedName = expression.asQualifiedName();
        if (qualifiedName == null) return false;

        var name = qualifiedName.join(separator);
        return name.equals(correctClass());
    }

    public abstract String correctClass();

    protected abstract void visitor(@NotNull ProblemsHolder holder, @NotNull PyAssignmentStatement node);
}
