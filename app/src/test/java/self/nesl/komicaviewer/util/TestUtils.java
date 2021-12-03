package self.nesl.komicaviewer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    public static void print(String string){
        System.out.println( string);
    }

    public static String currentFolder(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        return s;
    }

    public static String loadFile(String path){
        String ele=null;
        try {
            File f = new File(path);
            InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = read.read()) != -1){
                builder.append((char)ch);
            }
            ele=builder.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ele;
    }
}
