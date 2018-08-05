package org.luohuasheng.swagger.tools;

import com.github.kevinsawicki.http.HttpRequest;
import freemarker.template.TemplateException;
import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;
import org.luohuasheng.swagger.word.swagger2.Swagger2Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TemplateToolsTest {

    @org.junit.Test
    public void createFile() {
        String body = HttpRequest.get("https://api.migo-ebike.com/bike/v2/api-docs").body();
        Swagger20Parser parser = new Swagger20Parser();
        Swagger swagger = null;
        try {
            swagger = parser.parse(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Swagger2Word.exportDoc(swagger, "hello", null);
    }

    @org.junit.Test
    public void createFile2() {
        String body = HttpRequest.get("https://api.migo-ebike.com/bikerepair/v2/api-docs").body();
        Swagger20Parser parser = new Swagger20Parser();
        Swagger swagger = null;
        try {
            swagger = parser.parse(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Swagger2Word.exportDoc(swagger, "hello2", null);
    }
}