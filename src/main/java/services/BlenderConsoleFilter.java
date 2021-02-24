package services;

import data.BlenderInstance;
import data.BlenderSettings;
import com.intellij.execution.filters.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlenderConsoleFilter implements ConsoleFilterProvider {
    @NotNull
    @Override
    public Filter[] getDefaultFilters(@NotNull Project project) {
        return new Filter[]{new BlenderLinkFilter(project)};
    }

    static class BlenderLinkFilter implements Filter {

        private final Project project;
        private final BlenderSettings blenderSettings;

        public BlenderLinkFilter(Project project) {
            this.project = project;
            this.blenderSettings = BlenderSettings.getBlenderSettings(project);
        }

        @Nullable
        @Override
        public Result applyFilter(@NotNull String s, int end) {

            // TODO: Console Filter is currently disabled in plugin.xml.
            //       It will probably be included in future releases.

            String projectPath = project.getBasePath();
            if(projectPath == null) return null;

            int start = end - s.length();
            for(BlenderInstance instance : blenderSettings.getBlenderInstances()){
                if(instance.addonPath == null || instance.addonPath.equals("")) continue;
                int index = s.indexOf(instance.addonPath);
                if(index != -1){
                    int fileStart = start + index;
                    String blenderFile = s.substring(index).split("\",")[0];
                    String localFile = blenderFile.replace(instance.addonPath, projectPath).replace("\\", "/");
                    int line = Integer.parseInt(s.split("line ")[1].split(", ")[0]) - 1;
                    LazyFileHyperlinkInfo hyperlinkInfo = new LazyFileHyperlinkInfo(project, localFile, line, 0);
                    return new Result(fileStart - 5, fileStart + 10, hyperlinkInfo);
                }
            }
            return null;
        }
    }
}
