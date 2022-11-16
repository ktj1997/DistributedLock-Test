package com.example.lockstudy.mysql.named;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LockRepository {

  private static final String GET_LOCK_QUERY = "SELECT GET_LOCK(?, ?)";
  private static final String RELEASE_LOCK_QUERY = "SELECT RELEASE_LOCK(?)";

  public void getLock(Connection conn, String key, int timeOut) throws SQLException {
    PreparedStatement pstmt = conn.prepareStatement(GET_LOCK_QUERY);
    pstmt.setString(1, key);
    pstmt.setInt(2, timeOut);

    ResultSet rs = pstmt.executeQuery();
    if (!rs.next()) {
      throw new RuntimeException("Get Lock Fail");
    }
    log.info("Get Lock {} Success", key);
  }

  public void releaseLock(Connection conn, String key) throws SQLException {
    PreparedStatement pstmt = conn.prepareStatement(RELEASE_LOCK_QUERY);
    pstmt.setString(1, key);
    ResultSet rs = pstmt.executeQuery();
    if (!rs.next()) {
      throw new RuntimeException("Release Lock Fail");
    }
    log.info("Release Lock {} Success", key);
  }
}
