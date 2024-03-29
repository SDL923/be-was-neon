package manager;

import db.Database;
import model.User;
import org.junit.jupiter.api.*;
import request.HttpRequest;
import response.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterManagerTest {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    @BeforeEach
    void setRequestResponse() {
        httpRequest = new HttpRequest();
        httpResponse = new HttpResponse();
    }

    @Test
    @DisplayName("파싱한 회원가입 정보를 user 객체에 담은 후 DB에 저장해야 한다.")
    void storeUserAtDb() {
        httpRequest.storeStartLineData("POST /user/create HTTP/1.1");
        httpRequest.storeHeadersData("Content-Length: 59"); // headers 중에 필요한 정보만 전달
        httpRequest.storeBodyData("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");

        RegisterManager registerManager = new RegisterManager();
        registerManager.storeDatabase(registerManager.createUser(httpRequest)); // User 객체 생성 후 DB에 저장

        User user = Database.findUserById("javajigi");
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("박재성");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
    }

}
