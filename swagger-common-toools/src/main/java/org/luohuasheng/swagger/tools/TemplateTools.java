package org.luohuasheng.swagger.tools;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class TemplateTools {
    private static Configuration configuration = null;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("UTF-8");
    }

    public static void createFile(Map<String, Object> data, String templateFilePath, String outFilePath) throws IOException, TemplateException {
        int lastIndex = templateFilePath.lastIndexOf("/");
        String templateDir = null, templateFileName = null;
        if (lastIndex < 0) {
            templateDir = "/";
            templateFileName = templateFilePath;
        } else {
            templateDir = templateFilePath.substring(0, lastIndex);
            templateFileName = templateDir.substring(lastIndex);
        }
        configuration.setClassForTemplateLoading(TemplateTools.class, templateDir);
        Template t = configuration.getTemplate(templateFileName);
        File outFile = new File(outFilePath);
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        t.process(data, out);
        if (out != null) {
            out.close();
        }
    }
}
