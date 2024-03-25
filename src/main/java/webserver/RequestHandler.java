package webserver;

import java.io.*;
import java.net.Socket;

import manager.DefaultManager;
import manager.LoginManager;
import manager.LogoutManager;
import manager.RegisterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import request.RequestParser;
import response.HttpResponse;
import response.ResponseHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = RequestParser.readRequestMessage(in);

            HttpResponse httpResponse;

            // request message의 url에 맞는 manager 클래스 실행
            if(httpRequest.isUrlRegister()){
                RegisterManager registerManager = new RegisterManager(httpRequest);
                httpResponse = registerManager.responseMaker();
            }else if(httpRequest.isUrlLogin()){
                LoginManager loginManager = new LoginManager(httpRequest);
                httpResponse = loginManager.responseMaker();
            }else if(httpRequest.isUrlLogout()){
                LogoutManager logoutManager = new LogoutManager(httpRequest);
                httpResponse = logoutManager.responseMaker();
            }else{
                DefaultManager defaultManager = new DefaultManager(httpRequest);
                httpResponse = defaultManager.responseMaker();
            }

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(httpResponse);

            responseHandler.writeResponse(dos); // httpResponse 클래스의 정보를 dos에 write한다.
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}