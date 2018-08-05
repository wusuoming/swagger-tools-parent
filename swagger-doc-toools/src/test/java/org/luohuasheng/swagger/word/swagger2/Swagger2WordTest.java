package org.luohuasheng.swagger.word.swagger2;

import com.github.kevinsawicki.http.HttpRequest;
import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;
import org.junit.Test;

import java.io.IOException;

public class Swagger2WordTest {


    @Test
  public   void test() throws IOException {
        String body = HttpRequest.get("http://180.101.230.76:60104/v2/api-docs").body();
        Swagger20Parser parser = new Swagger20Parser();
        Swagger swagger = parser.parse(body);
        System.out.println(swagger);
    }

}