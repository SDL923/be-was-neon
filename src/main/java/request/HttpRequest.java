package request;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

public class HttpRequest {
    private static final String SPACE = " ";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    private static final String COLON_SPACE = ": ";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String URL_REGISTER = "/registration";
    private static final String URL_LOGIN = "/login";
    private static final String URL_LOGOUT = "/logout";

    private String startLine;
    private String body;

    private HashMap<String, String> startLineData;
    private HashMap<String, String> headersData;
    private HashMap<String, String> bodyData;

    public HttpRequest() {
        this.startLineData = new HashMap<String, String>();
        this.headersData = new HashMap<String, String>();
        this.bodyData = new HashMap<String, String>();
    }

    public void storeStartLineData(String startLine){ // start line HashMap에 저장
        this.startLine = startLine;
        parseStartLine(startLine);
    }

    public void storeHeadersData(String headerLine){ // headers 한줄마다 HashMap에 저장
        String[] splitHeaderLine = headerLine.split(COLON_SPACE);
        headersData.put(splitHeaderLine[0], splitHeaderLine[1]);
    }

    private void parseStartLine(String startLine){
        String[] splitStartLine = startLine.split(SPACE);
        startLineData.put("method", splitStartLine[0]);
        startLineData.put("url", splitStartLine[1]);
        startLineData.put("version", splitStartLine[2]);
    }

    public String parseHeaderCookie(String cookieKey){
        HashMap<String, String> cookieData = new HashMap<String, String>();
        for(String token : headersData.get("Cookie").split("[\\s;]+")){ // " "과 ";" 하나이상 반복된 패턴으로 split
            String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
            cookieData.put(splitInfo[0], splitInfo[1]); // 해쉬 맵에 정보 저장
        }
        return cookieData.get(cookieKey);
    }

    // ------------------------- request body 데이터 파싱&저장 -------------------------
    public void storeBodyData(String body){
        this.body = body;
        parseBodyData();
    }

     // 회원가입 정보 파싱
    public void parseBodyData() {
        for(String token : body.split(AMPERSAND)){
            String[] splitInfo = token.split(EQUAL); // 이름과 값을 = 로 분리
            try {
                bodyData.put(splitInfo[0], URLDecoder.decode(splitInfo[1], "UTF-8")); // 해쉬 맵에 정보 저장
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    // -------------------------  getter -------------------------
    public String getStartLineInfo(String startLineKey){
        return startLineData.get(startLineKey);
    }

    public String getHeaderInfo(String headerInfoKey){
        return headersData.get(headerInfoKey);
    }

    public String getBodyInfo(String bodyInfoKey){
        return bodyData.get(bodyInfoKey);
    }


    public int getContentLength(){
        return Integer.parseInt(headersData.get("Content-Length"));
    }

    public boolean isContentLengthExist(){ // Content-Length가 존재하고 0보다 큰지 확인
        return headersData.containsKey("Content-Length") && (getContentLength() > 0);
    }

    // ------------------------- is -------------------------
    public boolean isUrlRegister(){
        return startLineData.get("url").equals(URL_REGISTER);
    }

    public boolean isUrlLogin(){
        return startLineData.get("url").equals(URL_LOGIN);
    }

    public boolean isUrlLogout(){
        return startLineData.get("url").equals(URL_LOGOUT);
    }

    public boolean isMethodGet(){
        return startLineData.get("method").equals(METHOD_GET);
    }

    public boolean isMethodPost(){
        return startLineData.get("method").equals(METHOD_POST);
    }

}
