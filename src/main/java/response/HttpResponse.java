package response;

import java.io.IOException;
import java.util.HashMap;

public class HttpResponse {
    private static final String VERSION = "HTTP/1.1";
    private static final String ESCAPE_SEQUENCE = "\r\n";
    private static final String SPACE = " ";
    private static final String SEMI_COLON = ";";
    private static final String COOKIE_MAX_AGE = "60"; // 1분

    private String statusCode;
    private String statusMessage;
    private HashMap<String, String> headersData;
    byte[] body;

    public HttpResponse(){
        this.headersData = new HashMap<String, String>();
    }

    public String makeStartLine(){
        return (VERSION + SPACE + statusCode + SPACE +statusMessage + ESCAPE_SEQUENCE);
    }

    public String makeLocation() {
        return ("Location:" + SPACE + headersData.get("Location") + ESCAPE_SEQUENCE);
    }

    public String makeContentType() {
        return ("Content-Type:" + SPACE + headersData.get("Content-Type") + ESCAPE_SEQUENCE);
    }

    public String makeContentLength() {
        return ("Content-Length:" + SPACE + headersData.get("Content-Length") + ESCAPE_SEQUENCE);
    }

    public String makeCookie() {
        return ("Set-Cookie:" + SPACE + "sid=" + headersData.get("Set-Cookie") + SEMI_COLON + SPACE + "Path=/" + SEMI_COLON + SPACE + "Max-Age=" + COOKIE_MAX_AGE + ESCAPE_SEQUENCE);
    }

    public String makeEmptyLine(){
        return ESCAPE_SEQUENCE;
    }

    public byte[] getBody(){
        return body;
    }

    // ------------------------- setter -------------------------
    public void setStartLine(String statusCode, String statusMessage){
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void setLocation(String location){
        this.headersData.put("Location", location);
    }

    public void setContentType(String contentType){
        this.headersData.put("Content-Type", contentType);
    }

    public void setContentLength(String contentLength){
        this.headersData.put("Content-Length", contentLength);
    }

    public void setCookie(String cookie){
        this.headersData.put("Set-Cookie", cookie);
    }

    public void setBody(byte[] body) throws IOException {
        this.body = body;
    }

    // ------------------------- headers 값이 존재하는지 확인 -------------------------
    public boolean isLocationExist(){
        return headersData.containsKey("Location");
    }

    public boolean isContentTypeExist(){
        return headersData.containsKey("Content-Type");
    }

    public boolean isContentLengthExist(){
        return headersData.containsKey("Content-Length");
    }

    public boolean isCookieExist(){
        return headersData.containsKey("Set-Cookie");
    }

}
