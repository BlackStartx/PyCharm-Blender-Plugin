package util.core;

import java.io.*;

/**
 * Just some file function... the original class is way more complete...
 */
public class MyFileUtils {

    public static boolean createDirIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists())
            return file.mkdirs();
        return file.isDirectory();
    }

    public static String readText(String path) {
        try {
            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeText(String path, String text) {
        writeLines(path, new String[]{text});
    }

    public static void writeLines(String path, String[] lines) {
        try {
            Writer outWriter = new BufferedWriter(new FileWriter(path), 1024);
            for (String line : lines) outWriter.write(line + "\n");
            outWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
