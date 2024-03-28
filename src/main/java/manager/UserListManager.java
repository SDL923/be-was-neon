package manager;

import db.Database;
import db.Session;
import model.User;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;

import java.io.IOException;

public class UserListManager implements RequestManager {
    @Override
    public void getResponseSetter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(checkLoginStatus(httpRequest)){
            byte[] body = makeUserListHtml().getBytes();

            httpResponse.setStartLine("200", "OK");
            httpResponse.setContentType("html");
            httpResponse.setContentLength(String.valueOf(body.length));

            httpResponse.setBody(body);
        }else{
            httpResponse.setStartLine("302", "FOUND");
            httpResponse.setLocation("/login"); // 로그인 상태가 아니면 로그인 화면으로 redirect
        }
    }

    @Override
    public void postResponseSetter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
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

    public String makeUserListHtml(){
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><meta charset=\"UTF-8\" /><title>User List</title></head><body>"); // head

        htmlBuilder.append("<table border=\"1\">"); // table 만들기 시작
        htmlBuilder.append("<tr><th>ID</th><th>Password</th><th>Name</th><th>Email</th></tr>"); // table 목록

        for (User user : Database.findAll()) { // Database의 모든 User 정보 하나씩 가져와 table에 채워가기
            htmlBuilder.append("<tr>"); // table 한줄 시작
            htmlBuilder.append("<td>").append(user.getUserId()).append("</td>");
            htmlBuilder.append("<td>").append(user.getPassword()).append("</td>");
            htmlBuilder.append("<td>").append(user.getName()).append("</td>");
            htmlBuilder.append("<td>").append(user.getEmail()).append("</td>");
            htmlBuilder.append("</tr>"); // table 한줄 끝
        }

        htmlBuilder.append("</table>"); // table 끝
        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }
}
