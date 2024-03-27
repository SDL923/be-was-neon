package webserver;

import java.io.*;
import java.net.Socket;

import manager.*;
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

            HttpResponse httpResponse = new HttpResponse();

            RequestManager requestManager;
            requestManager = managerMapper(httpRequest);

            requestManager.responseMaker(httpRequest, httpResponse);

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(httpResponse);

            responseHandler.writeResponse(dos); // httpResponse 클래스의 정보를 dos에 write한다.
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private RequestManager managerMapper(HttpRequest httpRequest) {
        // request message의 url에 맞는 manager 클래스 맵핑
        if(httpRequest.isUrlRegister()){
            return new RegisterManager();
        }else if(httpRequest.isUrlLogin()){
            return new LoginManager();
        }else if(httpRequest.isUrlLogout()){
            return new LogoutManager();
        }else if(httpRequest.isUrlUserList()){
            return new UserListManager();
        }else{
            return new DefaultManager();
        }
    }

}