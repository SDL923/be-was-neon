package manager;

import db.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.FileInfo;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;
import db.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginManager implements RequestManager {
    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    @Override
    public void getResponseSetter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException { // 로그인 페이지로 이동하는 response 반환
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
    public void postResponseSetter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(isLoginSuccess(httpRequest.getBodyInfo("userId"), httpRequest.getBodyInfo("password"))){
            loginSuccessResponse(httpRequest, httpResponse);
        }else{
            loginFailResponse(httpResponse);
        }
    }

    private void loginSuccessResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException { // 로그인 성공했을 때 response 만들기
        String completePath = FileInfo.makeCompletePath("/main");
        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        body = showUserName(body, httpRequest); // name 포함한 html로 수정

        httpResponse.setStartLine("200", "OK");
        httpResponse.setContentType(ContentType.getContentType(FileInfo.getFileType(completePath)));
        httpResponse.setContentLength(String.valueOf(body.length));

        String sessionId = Session.storeSession(httpRequest.getBodyInfo("userId")); // session db에 저장
        httpResponse.setCookie(sessionId);

        httpResponse.setBody(body);
    }

    private void loginFailResponse(HttpResponse httpResponse) throws IOException { // 로그인 실패했을 때 response 만들기
        String completePath = FileInfo.makeCompletePath("/login/fail");
        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/login/fail/index.html");

        httpResponse.setBody(body);
    }

    private boolean isLoginSuccess(String inputUserId, String inputPassword){ // 로그인 성공 또는 실패 확인
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

    private byte[] showUserName(byte[] body, HttpRequest httpRequest){ // 로그인 되어있을 때 user name 표시
        String name = Database.findUserById(httpRequest.getBodyInfo("userId")).getName();
        return new String(body, StandardCharsets.UTF_8).replace("<!--사용자 이름-->", name).getBytes(); // 사용자 name으로 대체하기
    }

}
