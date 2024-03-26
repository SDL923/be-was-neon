package manager;

import db.Session;
import request.FileInfo;
import request.HttpRequest;
import response.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class LogoutManager {
    HttpRequest httpRequest;
    HttpResponse httpResponse;

    public LogoutManager(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        httpResponse = new HttpResponse();
    }

    public HttpResponse responseMaker() throws IOException { // 로그아웃 후 main 페이지로 가는 response 반환
        removeSessionId(); // 세션 id 삭제

        String completePath = FileInfo.makeCompletePath("/index.html");

        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/index.html");

        httpResponse.setBody(body);

        return httpResponse;
    }

    private void removeSessionId(){ // 저장되어 있는 session id 삭제
        Session.deleteSession(httpRequest.parseHeaderCookie("sid"));
        // 해당 session id가 없을 경우도 생각해야...
    }

}