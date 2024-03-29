package manager;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginManagerTest {

    @Test
    @DisplayName("DB에 있는 user 정보를 읽어서 user list html을 만들어야 한다.")
    void isLoginSuccessTest() {
        // test 할 user 생성 후 db에 저장
        User user1 = new User("daniel", "1234", "Daniel", "923daniel@naver.com");
        Database.addUser(user1);

        LoginManager loginManager = new LoginManager();

        // id pw 올바르게 입력
        assertThat(loginManager.isLoginSuccess("daniel", "1234")).isEqualTo(true);

        // id 잘못 입력
        assertThat(loginManager.isLoginSuccess("dan", "1234")).isEqualTo(false);

        // pw 잘못 입력
        assertThat(loginManager.isLoginSuccess("daniel", "5678")).isEqualTo(false);
    }
}
