package org.luohuasheng.swagger.tools;

import com.github.kevinsawicki.http.HttpRequest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DownloadTest {
    @Test
    public void download() throws IOException {
        HttpRequest request = HttpRequest.post("https://assets.processon.com/diagram_export/flow")
                .header("Cookie", "JSESSIONID=A8DD47EB0B44EEE5D78D853F1DC7EBBC.jvm1; zg_did=%7B%22did%22%3A%20%22164f8041e937aa-0c994ed98e8a7e8-4a5369-7e900-164f8041e95b7d%22%7D; zg_3f37ba50e54f4374b9af5be6d12b208f=%7B%22sid%22%3A%201533178139738%2C%22updated%22%3A%201533178229595%2C%22info%22%3A%201533169376926%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22landHref%22%3A%20%22https%3A%2F%2Fwww.processon.com%2F%22%7D; _ga=GA1.2.125771009.1533169379; _gid=GA1.2.1242911552.1533169379; processon_userKey=5b624f92e4b0555b39cfb314; _sid=331152c50844e492803dda0c6f1865b9")
                .contentLength(146237)
                .header("Connection","keep-alive")
                .form("chartId", "5b6258b5e4b025cf493211b4")
                .form("definition", "{\"page\":{\"showGrid\":false,\"gridSize\":15,\"orientation\":\"portrait\",\"height\":2100,\"backgroundColor\":\"transparent\",\"width\":3000,\"padding\":60},\"elements\":{\"15968a43e2d59f\":{\"textBlock\":[{\"position\":{\"w\":\"w+40\",\"h\":\"30\",\"y\":\"h\",\"x\":\"-20\"},\"text\":\"log\"}],\"lineStyle\":{\"lineWidth\":0},\"link\":\"\",\"children\":[],\"parent\":\"\",\"attribute\":{\"linkable\":true,\"visible\":true,\"container\":false,\"rotatable\":true,\"markerOffset\":5,\"collapsable\":false,\"collapsed\":false},\"fontStyle\":{},\"resizeDir\":[\"tl\",\"tr\",\"br\",\"bl\"],\"dataAttributes\"….0625},\"dataAttributes\":[],\"locked\":false,\"points\":[{\"y\":788.75,\"x\":1212.0625}],\"group\":\"\",\"props\":{\"zindex\":199}},\"159683b1d855f5\":{\"id\":\"159683b1d855f5\",\"to\":{\"id\":\"159683a4da3ca4\",\"y\":747.5,\"angle\":1.5707963267948968,\"x\":626},\"text\":\"\",\"linkerType\":\"broken\",\"name\":\"linker\",\"lineStyle\":{\"lineColor\":\"0,204,102\"},\"points\":[{\"y\":714.75,\"x\":626},{\"y\":714.75,\"x\":626}],\"locked\":false,\"dataAttributes\":[],\"from\":{\"id\":\"15968398439f95\",\"y\":682,\"angle\":4.71238898038469,\"x\":626},\"group\":\"\",\"props\":{\"zindex\":175}}}}")
                .form("ignore", "definition")
                .form("title", "阿里云网络拓扑图")
                .form("type", "svg");

        request.stream();
    }

}
