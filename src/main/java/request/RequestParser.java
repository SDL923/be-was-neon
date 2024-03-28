package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public static HttpRequest readRequestMessage(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        HttpRequest httpRequest = new HttpRequest();

        readStartLine(br, httpRequest);
        readHeaders(br, httpRequest);
        if(httpRequest.isContentLengthExist()){
            readBody(br, httpRequest);
        }
        return httpRequest;
    }

    private static void readStartLine(BufferedReader br, HttpRequest httpRequest) throws IOException {
        String line = br.readLine();
        httpRequest.storeStartLineData(line); // start line 저장
        logger.debug("request line : {}", line);
    }

    private static void readHeaders(BufferedReader br, HttpRequest httpRequest) throws IOException {
        String line = br.readLine();
        while(!line.isEmpty()){ // 나머지 headers 순회
            httpRequest.storeHeadersData(line); // header 저장
            logger.debug("request header : {}", line);
            line = br.readLine();
        }
    }

    private static void readBody(BufferedReader br, HttpRequest httpRequest) throws IOException {
        char[] buffer = new char[httpRequest.getContentLength()];
        br.read(buffer); // context length 만큼 읽기
        logger.debug("request body : {}", new String(buffer));
        httpRequest.storeBodyData(new String(buffer)); // body 저장
    }
}