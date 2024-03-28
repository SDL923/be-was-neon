package manager;

import request.HttpRequest;
import response.HttpResponse;

import java.io.IOException;

public interface RequestManager {
    default void fillResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(httpRequest.isMethodGet()){
            manageGet(httpRequest, httpResponse);
        }
        if(httpRequest.isMethodPost()){
            managePost(httpRequest, httpResponse);
        }
    }

    void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

    void managePost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

}
