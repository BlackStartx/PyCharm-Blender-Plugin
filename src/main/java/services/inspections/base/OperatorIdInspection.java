package services.inspections.base;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.python.psi.PyAssignmentStatement;
import com.jetbrains.python.psi.PyElementVisitor;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl;
import org.jetbrains.annotations.NotNull;

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
