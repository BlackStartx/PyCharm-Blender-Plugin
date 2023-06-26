package util.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Just some file function... the original class is way more complete...
 */
public class MyFileUtils {

    public static boolean cantCreateDirectory(String path) {
        File file = new File(path);
        return file.exists() ? !file.isDirectory() : !file.mkdirs();
    }

    public static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(Path path, String text) {
        try {
            Files.write(path, text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
