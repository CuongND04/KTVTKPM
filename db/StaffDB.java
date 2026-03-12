package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Staff;

public class StaffDB {
  public Staff getFirstStaff() {
    String sql = "SELECT id, name, username, password, email, role FROM staff ORDER BY id LIMIT 1";

    try (Connection conn = DBHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new Staff(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("username"),
              rs.getString("password"),
              rs.getString("email"),
              rs.getString("role"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}