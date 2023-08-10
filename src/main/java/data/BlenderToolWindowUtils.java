package data;

import com.intellij.openapi.application.PathManager;

import java.io.File;

public class BlenderToolWindowUtils {
    public static File getEggFile() {
        String ide = PathManager.getHomePath();
        return new File(ide + File.separator + "debug-eggs" + File.separator + "pydevd-pycharm.egg");
    }
}
