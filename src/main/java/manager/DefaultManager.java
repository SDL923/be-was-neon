package manager;

import request.FileInfo;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DefaultManager implements RequestManager { // url이 앞의 모든 경우에 포함되지 않을 때, 디폴트로 처리하는 Manager
    @Override
    public void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String completePath = FileInfo.makeCompletePath(httpRequest.getStartLineInfo("url"));
        String contextType = ContentType.getContentType(FileInfo.getFileType(completePath));

        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("200", "OK");
        httpResponse.setContentType(contextType);
        httpResponse.setContentLength(String.valueOf(body.length));

        httpResponse.setBody(body);
    }

    @Override
    public void managePost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // bad request
        byte[] body = "<h1>404 Not Found</h1>".getBytes();
        httpResponse.setStartLine("404", "Not Found");
        httpResponse.setContentType(ContentType.getContentType("html"));
        httpResponse.setContentLength(String.valueOf(body.length));

        httpResponse.setBody(body);
    }

}
