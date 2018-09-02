package org.luohuasheng.swagger.tools;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TencentTest {

    @Test
    public void export() throws Exception {
        Document document = Jsoup.parse(new File(TencentTest.class.getResource("/tencent.html").getFile()), "utf8");
        List<List<String>> contents = Lists.newArrayList();
        int i = 0;
        for (Element elementsByClass : document.getElementsByClass("menu-area")) {
            Element groupTitle = elementsByClass.selectFirst(".menu-area-tit");
            Elements groupItems = elementsByClass.getElementsByClass("menu-item-tit");
            for (Element groupItem : groupItems) {
                List<String> content = Lists.newArrayList();
                Element itemTag = groupItem.selectFirst("a");
                String title = itemTag.text();
                String href = itemTag.attr("href");
                content.add(groupTitle.text());
                content.add(title);
                content.add(href);
                contents.add(content);
                i++;
            }
        }
        //获取数据

        //excel标题
        String[] title = {"分组名称", "功能名称", "功能说明地址"};


        //sheet名
        String sheetName = "学生信息表";


//创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, contents, null);

//响应到客户端
        try {
            OutputStream os = new FileOutputStream("tencent.xls");
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
