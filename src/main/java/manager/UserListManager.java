package manager;

import db.Database;
import db.Session;
import model.User;
import request.FileInfo;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UserListManager {
    HttpRequest httpRequest;
    HttpResponse httpResponse;

    public UserListManager(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        httpResponse = new HttpResponse();
    }

    public HttpResponse responseMaker() throws IOException {

        if(checkLoginStatus()){
//            String completePath = FileInfo.makeCompletePath("/");
//            String contextType = ContentType.getContentType(FileInfo.getFileType(completePath));

            byte[] body = makeUserListHtml().getBytes();

            httpResponse.setStartLine("200", "OK");
            httpResponse.setContentType("html");
            httpResponse.setContentLength(String.valueOf(body.length));

            httpResponse.setBody(body);
        }else{
            String completePath = FileInfo.makeCompletePath("/login");
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



        return httpResponse;
    }

    public boolean checkLoginStatus(){
        return (httpRequest.isCookieExist() && Session.isSessionExist(httpRequest.parseHeaderCookie("sid")));
    }

    public String makeUserListHtml(){
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>User List</title></head><body>"); // head

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
