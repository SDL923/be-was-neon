package response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {
    HttpResponse httpResponse;

    public ResponseHandler(HttpResponse httpResponse){
        this.httpResponse = httpResponse;
    }

    public String getResponseHeader(){ // HttpResponseHeader의 정보를 이용해 response header를 만든다
        StringBuilder buildResponse = new StringBuilder();

        buildResponse.append(httpResponse.makeStartLine());
        if(httpResponse.isLocationExist()){
            buildResponse.append(httpResponse.makeLocation());
        }
        if(httpResponse.isContentTypeExist()){
            buildResponse.append(httpResponse.makeContentType());
        }
        if(httpResponse.isContentLengthExist()){
            buildResponse.append(httpResponse.makeContentLength());
        }
        if(httpResponse.isCookieExist()){
            buildResponse.append(httpResponse.makeCookie());
        }
        return buildResponse.toString();
    }

    public void writeResponse(DataOutputStream dos) throws IOException {
        dos.writeBytes(getResponseHeader()); // StartLine + Headers
        dos.writeBytes(httpResponse.makeEmptyLine()); // 공백
        if(httpResponse.getBody() != null) { // body가 있으면 write
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length); // Body
        }
    }
}
