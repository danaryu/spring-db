package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * jdbc drivermanager
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        con = getConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.info("error");
            }
        }

        if (stmt != null) {
            try {
                stmt.close(); // Exception이 터지면.. close()가 호출이 되지 않는다!
            } catch (SQLException e) {
                log.info("error");
            }
        }

        if (con != null) {
            try {
                con.close(); // 외부 resource를 사용
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}
