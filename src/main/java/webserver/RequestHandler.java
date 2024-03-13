package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.StringUtils;
import model.User;
import db.Database;

public class RequestHandler implements Runnable {
    private static final String REGISTER_ACTION = "/user/create";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String requestLine = br.readLine();
            logger.debug("request method : {}", requestLine);
            if(checkRegisterInput(requestLine)){
                Database.addUser(parseRegisterRequest(requestLine));
            }

            // 요청 받은 URL을 파싱하여 파일 경로를 결정한다.
            String requestURL = StringUtils.separatePath(requestLine);
            String filePath = StringUtils.makeCompletePath(requestURL);

            requestLine = br.readLine();
            while(!requestLine.isEmpty()){ // 나머지 header 출력
                logger.debug("request header : {}", requestLine);
                requestLine = br.readLine();
            }

            // 파일이 존재하면 해당 파일을 읽어 응답.
            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] fileContent = new byte[(int) file.length()];
                fis.read(fileContent);
                fis.close();

                response200Header(dos, fileContent.length);
                responseBody(dos, fileContent);
            } else {
                // 파일이 존재하지 않으면 404 응답.
                byte[] notFoundContent = "<h1>404 Not Found</h1>".getBytes();
                response404Header(dos, notFoundContent.length);
                responseBody(dos, notFoundContent);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private boolean checkRegisterInput(String startLine){ // 회원가입에서 보낸 GET인지 확인
        return startLine.contains(REGISTER_ACTION);
    }

    private User parseRegisterRequest(String startLine) throws UnsupportedEncodingException {
        String[] splitLine = startLine.split("[=& ]");

        // 디코딩하여 한글로 변환
        String userId = URLDecoder.decode(splitLine[2], "UTF-8");
        String password = URLDecoder.decode(splitLine[4], "UTF-8");
        String name = URLDecoder.decode(splitLine[6], "UTF-8");
        String email = URLDecoder.decode(splitLine[8], "UTF-8");

        return new User(userId, password, name, email);
    }
}
