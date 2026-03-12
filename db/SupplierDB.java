package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Supplier;

public class SupplierDB {
  public ArrayList<Supplier> searchSupplier(String keyword) {
    ArrayList<Supplier> suppliers = new ArrayList<>();
    String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
    String likeKeyword = "%" + normalized + "%";

    String sql = """
        SELECT id, name, address, phone, email
        FROM supplier
        WHERE ? = ''
           OR LOWER(name) LIKE ?
           OR LOWER(phone) LIKE ?
           OR LOWER(email) LIKE ?
        ORDER BY id
        """;

    try (Connection conn = DBHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, normalized);
      ps.setString(2, likeKeyword);
      ps.setString(3, likeKeyword);
      ps.setString(4, likeKeyword);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          suppliers.add(new Supplier(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("address"),
              rs.getString("phone"),
              rs.getString("email")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return suppliers;
  }
}