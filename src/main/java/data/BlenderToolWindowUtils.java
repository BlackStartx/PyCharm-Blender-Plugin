package data;

import java.io.File;
import java.net.URL;

public class BlenderToolWindowUtils {

    public static File getJarPath() {
        URL x = BlenderToolWindowUtils.class.getClassLoader().getResource("debugger");
        if (x == null) return null;
        return new File(x.getPath().split("/", 2)[1].split(".jar!")[0] + ".jar");
    }

    public static File getIdePath() {
        File jar = getJarPath();
        if (jar == null) return null;
        return getJarPath().getParentFile().getParentFile();
    }

    public static File getEggFile() {
        File ide = getIdePath();
        if (ide == null) return null;
        return new File(ide.getPath() + File.separator + "debug-eggs" + File.separator + "pydevd-pycharm.egg");
    }
}
