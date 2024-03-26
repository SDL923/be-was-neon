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

public class RegisterManager {
    private static final Logger logger = LoggerFactory.getLogger(RegisterManager.class);

    HttpRequest httpRequest;
    HttpResponse httpResponse;

    public RegisterManager(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        httpResponse = new HttpResponse();
    }

    public HttpResponse responseMaker() throws IOException {
        if(httpRequest.isMethodGet()){
            getResponseSetter();
        }
        if(httpRequest.isMethodPost()){
            postResponseSetter();
        }
        return httpResponse;
    }

    private void getResponseSetter() throws IOException { // 회원가입 페이지로 이동하는 response 반환
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

    private void postResponseSetter() throws IOException { // 회원가입 후 main 페이지로 redirect 하는 response 반환
        storeDatabase(createUser()); // request 정보로 User 객체 생성 후 db에 저장
        logger.info("회원가입 성공!");

        String completePath = FileInfo.makeCompletePath("/index.html");

        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/index.html");

        httpResponse.setBody(body);
    }

    private User createUser(){ // httpRequest 정보를 통해 User객체 생성
        return new User(
                httpRequest.getBodyInfo("userId"),
                httpRequest.getBodyInfo("password"),
                httpRequest.getBodyInfo("name"),
                httpRequest.getBodyInfo("email"));
    }

    // 회원가입 데이터 DB에 추가
    private void storeDatabase(User user){ // User 객체를 db에 저장
        Database.addUser(user);
    }
}
