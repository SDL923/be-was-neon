package manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.FileInfo;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;
import model.User;
import db.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RegisterManager implements RequestManager { // url이 "/register"일때 처리하는 Manager
    private static final Logger logger = LoggerFactory.getLogger(RegisterManager.class);

    @Override
    public void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException { // 회원가입 페이지로 이동하는 response 반환
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
    public void managePost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException { // 회원가입 후 login 페이지로 redirect 하는 response 반환
        storeDatabase(createUser(httpRequest)); // request 정보로 User 객체 생성 후 db에 저장
        logger.info("회원가입 성공!");

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/login"); // 회원가입 하면 login 페이지로 redirect
    }

    private User createUser(HttpRequest httpRequest){ // httpRequest 정보를 통해 User객체 생성
        return new User(
                httpRequest.getBodyInfo("userId"),
                httpRequest.getBodyInfo("password"),
                httpRequest.getBodyInfo("name"),
                httpRequest.getBodyInfo("email"));
    }

    private void storeDatabase(User user){ // User 객체를 db에 저장
        Database.addUser(user);
    }
}
