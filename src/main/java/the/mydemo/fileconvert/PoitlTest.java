package the.mydemo.fileconvert;

import com.deepoove.poi.XWPFTemplate;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import the.mydemo.fileconvert.model.WithdrawData;

import java.io.*;

/**
 * Created by xulh on 2019/12/9.
 */
public class PoitlTest {

    public static void main(String[] args) throws IOException {


        WithdrawData data = new WithdrawData();
        data.setId("T000002");
        data.setCreateTime(System.currentTimeMillis()+"");
        data.setUserName("中白菜有限公司");
        long start = System.currentTimeMillis();
        XWPFTemplate template = XWPFTemplate.compile("D:\\temp\\交易电子回单转账模板.docx");
        /*template.render(new HashMap<String, Object>(){{
            put("ID", "T000001");
            put("createTime", System.currentTimeMillis()+"");
            put("userName", "小白菜有限公司");
        }});*/
        template.render(data);
        FileOutputStream out = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
//            out = new FileOutputStream("D:\\temp\\out_template.docx");
        template.write(outputStream);
//        out.flush();
//        out.close();
        template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end1 = System.currentTimeMillis();

//        InputStream docxInputStream = new FileInputStream("D:\\temp\\out_template.docx");
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ByteArrayOutputStream pdfOutPutStream = new ByteArrayOutputStream();
        try {
            OutputStream fileOutputStream  =  new FileOutputStream("D:\\temp\\电子回单.pdf");
            IConverter converter  =  LocalConverter.builder().build();
            converter.convert(input).as(DocumentType.DOCX).to(pdfOutPutStream).as(DocumentType.PDF).execute();
//            outputStream.close();
            converter.shutDown();
            System.out.println("success");
        } catch (Exception e1){

        }
        long end2 = System.currentTimeMillis();
        System.out.println("耗时:"+(end1-start)+"转换耗时:"+(end2-end1));
        System.out.println("大小:"+pdfOutPutStream.size());
    }
}
