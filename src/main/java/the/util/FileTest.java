package the.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by xulh on 2019/11/27.
 */
public class FileTest {

    public static void main(String[] args) {
        File file = new File("D:\\工作\\需求\\快捷改造\\log分割1.txt");
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    String[] log = text.split(",");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
