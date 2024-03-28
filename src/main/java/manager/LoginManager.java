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

public class LoginManager implements RequestManager { // url이 "/login"일때 처리하는 Manager
    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    @Override
    public void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException { // 로그인 페이지로 이동하는 response 반환
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
    public void managePost(HttpRequest httpRequest, HttpResponse httpResponse) { // 로그인 성공/실패로 나누어 동작
        if(isLoginSuccess(httpRequest.getBodyInfo("userId"), httpRequest.getBodyInfo("password"))){
            loginSuccessResponse(httpRequest, httpResponse);
        }else{
            loginFailResponse(httpResponse);
        }
    }

    private void loginSuccessResponse(HttpRequest httpRequest, HttpResponse httpResponse) { // 로그인 성공했을 때
        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/myPage");

        String sessionId = Session.storeSession(httpRequest.getBodyInfo("userId")); // sid 생성 후 session db에 저장
        httpResponse.setCookie(sessionId); // cookie 설정
    }

    private void loginFailResponse(HttpResponse httpResponse) { // 로그인 실패했을 때
        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/login/fail");
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
}
