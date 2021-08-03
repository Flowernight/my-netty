package the.mydemo.fileconvert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;


/**
 * Created by xulh on 2019/12/9.
 */
public class WordToPDF {

    public static void main(String[] args) {
        File  inputWord  =  new  File("D:\\工作\\需求\\电子回单\\电子回单需求12.6\\交易电子回单转账模板.docx");
        File  outputFile  =  new  File("D:\\test1.pdf");
        try    {
            InputStream docxInputStream  =  new FileInputStream(inputWord);
            OutputStream outputStream  =  new FileOutputStream(outputFile);
            IConverter converter  =  LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            converter.shutDown();
            System.out.println("success");
        }  catch  (Exception  e)  {
            e.printStackTrace();
        }
    }

}
