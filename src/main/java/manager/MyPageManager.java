package manager;

import db.Database;
import db.Session;
import request.HttpRequest;
import request.FileInfo;
import response.ContentType;
import response.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MyPageManager implements RequestManager{ // url이 "/myPage"일때 처리하는 Manager
    @Override
    public void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(checkLoginStatus(httpRequest)){
            String completePath = FileInfo.makeCompletePath("/myPage");
            File file = new File(completePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] body = fis.readAllBytes();
            fis.close();

            body = showUserName(body, httpRequest); // name 포함한 html로 수정

            httpResponse.setStartLine("200", "OK");
            httpResponse.setContentType(ContentType.getContentType(FileInfo.getFileType(completePath)));
            httpResponse.setContentLength(String.valueOf(body.length));

            httpResponse.setBody(body);
        }else{ // login 상태가 아니면 login 페이지로 redirect
            httpResponse.setStartLine("302", "FOUND");
            httpResponse.setLocation("/login");
        }
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

    public boolean checkLoginStatus(HttpRequest httpRequest){ // 현재 request의 cookie가 유효한 sid를 가지고있는지 확인
        return (httpRequest.isCookieExist() && Session.isSessionExist(httpRequest.parseHeaderCookie("sid")));
    }

    private byte[] showUserName(byte[] body, HttpRequest httpRequest){ // 로그인 되어있을 때 user name 표시
        String userId = Session.getSessionUserId(httpRequest.parseHeaderCookie("sid")); // sid로 userId를 구하고
        String name = Database.findUserById(userId).getName(); // userId로 db에서 name을 구한다.
        return new String(body, StandardCharsets.UTF_8).replace("<!--사용자 이름-->", name).getBytes(); // 사용자 name으로 대체하기
    }
}
