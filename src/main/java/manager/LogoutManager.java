package manager;

import db.Session;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;

import java.io.IOException;

public class LogoutManager implements RequestManager { // url이 "/logout"일때 처리하는 Manager
    @Override
    public void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        removeSessionId(httpRequest.parseHeaderCookie("sid")); // 세션 id 삭제

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/"); // 기본 index.html 파일로 redirect
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

    private void removeSessionId(String sid){ // 저장되어 있는 session id 삭제
        Session.deleteSession(sid);
        // 해당 session id가 없을 경우도 생각해야...
    }
}