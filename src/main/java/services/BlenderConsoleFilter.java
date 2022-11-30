package services;

import data.BlenderInstance;
import settings.BlenderSettings;
import com.intellij.execution.filters.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlenderConsoleFilter implements ConsoleFilterProvider {
    @Override
    public Filter @NotNull [] getDefaultFilters(@NotNull Project project) {
        return new Filter[]{new BlenderLinkFilter(project)};
    }

    public static class BlenderLinkFilter implements Filter {

        private final Project project;
        private final BlenderSettings blenderSettings;

        public BlenderLinkFilter(Project project) {
            this.project = project;
            this.blenderSettings = BlenderSettings.getBlenderSettings(project);
        }

        @Nullable
        @Override
        public Result applyFilter(@NotNull String s, int end) {
            String projectPath = project.getBasePath();
            if (projectPath == null) return null;

            int start = end - s.length();
            for (BlenderInstance instance : blenderSettings.getBlenderInstances()) {
                if (instance.addonPath == null || instance.addonPath.equals("")) continue;
                int index = s.indexOf(instance.addonPath);
                if (index != -1) {
                    String afterPathUnknown = s.substring(index + instance.addonPath.length());
                    String afterPath = afterPathUnknown.split("[\":]")[0];
                    String blenderFile = instance.addonPath + afterPath;
                    String localFile = blenderFile.replace(instance.addonPath, projectPath).replace("\\", "/");

                    int fileStart = start + index;
                    int line = tryGetLine(s);
                    LazyFileHyperlinkInfo hyperlinkInfo = new LazyFileHyperlinkInfo(project, localFile, line, 0);
                    return new Result(fileStart, fileStart + blenderFile.length(), hyperlinkInfo);
                }
            }
            return null;
        }

        /**
         * An awful way to obtain the line from two different type of strings.
         *
         * @param msg the message from the console.
         * @return the line the msg is referring to.
         */
        private int tryGetLine(String msg) {
            try {
                return Integer.parseInt(msg.split("line ")[1].split(", ")[0]) - 1;
            } catch (Exception ignored) {
            }
            try {
                return Integer.parseInt(msg.split(":")[2].trim()) - 1;
            } catch (Exception ignored) {
            }
            return 0;
        }
    }
}
