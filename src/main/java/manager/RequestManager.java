package manager;

import request.HttpRequest;
import response.HttpResponse;

import java.io.IOException;

public interface RequestManager {
    default void responseMaker(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(httpRequest.isMethodGet()){
            getResponseSetter(httpRequest, httpResponse);
        }
        if(httpRequest.isMethodPost()){
            postResponseSetter(httpRequest, httpResponse);
        }
    }

    void getResponseSetter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

    void postResponseSetter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

}
