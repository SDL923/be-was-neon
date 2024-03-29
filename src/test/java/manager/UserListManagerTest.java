package manager;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserListManagerTest {

    @Test
    @DisplayName("DB에 있는 user 정보를 읽어서 user list html을 만들어야 한다.")
    void makeUserListHtmlTest() {
        // test 할 user 생성 후 db에 저장
        User user1 = new User("daniel", "1234", "Daniel", "923daniel@naver.com");
        User user2 = new User("rachel", "1234", "Rachel", "427rachel@naver.com");
        Database.addUser(user1);
        Database.addUser(user2);

        UserListManager userListManager = new UserListManager();

        String expectedHtml = "<html><head><meta charset=\"UTF-8\" /><title>User List</title></head><body>"
                + "<table border=\"1\">"
                + "<tr><th>ID</th><th>Password</th><th>Name</th><th>Email</th></tr>"
                + "<tr><td>daniel</td><td>1234</td><td>Daniel</td><td>923daniel@naver.com</td></tr>"
                + "<tr><td>rachel</td><td>1234</td><td>Rachel</td><td>427rachel@naver.com</td></tr>"
                + "</table></body></html>";

        assertThat(userListManager.makeUserListHtml()).isEqualTo(expectedHtml);
    }
}
