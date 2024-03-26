package manager;

import db.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.FileInfo;
import request.HttpRequest;
import request.RequestParser;
import response.ContentType;
import response.HttpResponse;
import db.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LoginManager {
    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    HttpRequest httpRequest;
    HttpResponse httpResponse;

    public LoginManager(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        httpResponse = new HttpResponse();
    }

    public HttpResponse responseMaker() throws IOException {
        if(httpRequest.getStartLineInfo("method").equals("GET")){
            getResponseSetter();
        }
        if(httpRequest.getStartLineInfo("method").equals("POST")){
            postResponseSetter();
        }
        return httpResponse;
    }

    private void getResponseSetter() throws IOException {  // 로그인 페이지로 이동하는 response 반환
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

    private void postResponseSetter() throws IOException { // 로그인 결과에 따라 response 반환
        if(isLoginSuccess()){
            loginSuccessResponse();
        }else{
            loginFailResponse();
        }
    }

    private void loginSuccessResponse() throws IOException { // 로그인 성공했을 때 response 만들기
        String completePath = FileInfo.makeCompletePath("/main");
        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/main/index.html");
        String sessionId = Session.storeSession(httpRequest.getBodyInfo("userId")); // session db에 저장
        httpResponse.setCookie(sessionId);

        httpResponse.setBody(body);
    }

    private void loginFailResponse() throws IOException { // 로그인 실패했을 때 response 만들기
        String completePath = FileInfo.makeCompletePath("/login/fail");
        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/login/fail/index.html");

        httpResponse.setBody(body);
    }

    private boolean isLoginSuccess(){ // 로그인 성공 또는 실패 확인
        String inputUserId = httpRequest.getBodyInfo("userId");
        String inputPassword = httpRequest.getBodyInfo("password");

        if(Database.findUserById(inputUserId) == null){ // 해당 userId의 데이터가 없으면 false
            logger.error("아이디가 존재하지 않습니다! (input ID: {})", inputUserId);
            return false;
        }
        String dbPassword = Database.findUserById(inputUserId).getPassword(); // db에서 id에 맞는 password 가져오기
        if(!inputPassword.equals(dbPassword)){ // password가 일치하지 않으면
            logger.error("비밀번호가 일치하지 않습니다! (input ID: {}, input Password: {})", inputUserId, inputPassword);
            return false;
        }

        logger.info("로그인 성공! (login ID: {})", inputUserId);
        return true;
    }
}
