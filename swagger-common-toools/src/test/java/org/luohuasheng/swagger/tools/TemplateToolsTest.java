package org.luohuasheng.swagger.tools;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;

public class TemplateToolsTest {

    @org.junit.Test
    public void createFile() throws IOException, TemplateException {
        TemplateTools.createFile(new HashMap(),"","./hello.doc");
    }
}