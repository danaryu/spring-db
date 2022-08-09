package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.ldap.Control;
import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class); // 예외가 올라온 것을 확인 가능
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException { // 던져던져!
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException { // 여러개의 예외처리
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }


}
