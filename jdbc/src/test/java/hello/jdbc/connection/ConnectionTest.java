package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection ={}, class={}", con1, con1.getClass());
        log.info("connection ={}, class={}", con2, con2.getClass());
    }
    // connection을 획득할 대마다 파라미터를 계속 전달해야한다.

    @Test
    void dataSourceDriverManager() throws SQLException {
        // DriverManagerDataSource - 항상 새로운 커넥션을 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    // 별도의 thread를 통해 connection을 채워줘야 어플리케이션 실행에 영향을 미치지않는다.
    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //커넥션 풀링 //by hikariCP
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }


    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();

        log.info("connection ={}, class={}", con1, con1.getClass());
        log.info("connection ={}, class={}", con2, con2.getClass());
    }
    // 처음 객체를 생성할 때만 필요한 파라미터를 넘겨두고, 커넥 션을 획득할 대는 단순히 getConnection만 호추하면 됟나.
    // 설정과 사용의 분리
    // 설정 : Datasource를 만들고 필요한 속성들을 사용해서 URL, USERNAME, PASSWORD같은 부분을 입력하는 것
    // 사용 : 설정은 신경쓰지 않고, Datasource의 getConnection만 호출해서 사용
    // 결합도를 높일 수 있다!!
    // Repository는 datasource에만 의존할 수 있다
}
