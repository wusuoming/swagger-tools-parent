package org.luohuasheng.swagger.tools.controller;


import com.github.kevinsawicki.http.HttpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;
import org.luohuasheng.swagger.word.swagger2.Swagger2Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


@RestController
@RequestMapping("/swagger2")
@Api(description = "Swagger导出文档工具", tags = "Swagger工具")
public class SwaggerToolsController {


    private static final Logger logger = LoggerFactory.getLogger(SwaggerToolsController.class);


    @ApiOperation(value = "根据URL生成Word文档", notes = "根据URL生成Word文档")
    @RequestMapping(value = "/getSwaggerDocForUrl", method = RequestMethod.GET)
    public void getSwaggerDocForUrl(@ApiParam("Swagger2地址") @RequestParam String url, HttpServletResponse response) {

        String fileName = "file.docx";
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try {
            String body = HttpRequest.get(url).body();
            Swagger20Parser parser = new Swagger20Parser();
            Swagger swagger = parser.parse(body);
            Swagger2Word.exportDoc(swagger, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
