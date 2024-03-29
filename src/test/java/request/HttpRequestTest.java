package request;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    private HttpRequest httpRequest;

    @BeforeEach
    void setHttpRequest() {
        httpRequest = new HttpRequest();
    }

    @Test
    @DisplayName("StartLine에서 method를 가져와야 한다.")
    void getMethod(){
        httpRequest.storeStartLineData("GET /registration HTTP/1.1");
        assertThat(httpRequest.getStartLineInfo("method")).isEqualTo("GET");
    }

    @Test
    @DisplayName("StartLine에서 url을 가져와야 한다.")
    void getUrl(){
        httpRequest.storeStartLineData("GET /registration HTTP/1.1");
        assertThat(httpRequest.getStartLineInfo("url")).isEqualTo("/registration");
    }

    @Test
    @DisplayName("StartLine에서 version을 가져와야 한다.")
    void getVersion(){
        httpRequest.storeStartLineData("GET /registration HTTP/1.1");
        assertThat(httpRequest.getStartLineInfo("version")).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("POST 방식으로 전달된 회원가입 정보를 파싱할 수 있어야 한다.")
    void parsePostBodyData() {
        httpRequest.storeStartLineData("POST /user/create HTTP/1.1");
        httpRequest.storeHeadersData("Content-Length: 59"); // headers 중에 필요한 정보만 전달
        httpRequest.storeBodyData("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");

        assertThat(httpRequest.getBodyInfo("userId")).isEqualTo("javajigi");
        assertThat(httpRequest.getBodyInfo("password")).isEqualTo("password");
        assertThat(httpRequest.getBodyInfo("name")).isEqualTo("박재성");
        assertThat(httpRequest.getBodyInfo("email")).isEqualTo("javajigi@slipp.net");
    }

}