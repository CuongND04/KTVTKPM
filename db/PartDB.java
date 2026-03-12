package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Part;

public class PartDB {
  public ArrayList<Part> searchPart(String keyword) {
    ArrayList<Part> parts = new ArrayList<>();
    String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
    String likeKeyword = "%" + normalized + "%";

    String sql = """
        SELECT id, name, description, quantity, buy_price, sell_price
        FROM part
        WHERE ? = ''
           OR LOWER(name) LIKE ?
           OR LOWER(description) LIKE ?
        ORDER BY id
        """;

    try (Connection conn = DBHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, normalized);
      ps.setString(2, likeKeyword);
      ps.setString(3, likeKeyword);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          parts.add(new Part(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("description"),
              rs.getInt("quantity"),
              rs.getFloat("buy_price"),
              rs.getFloat("sell_price")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return parts;
  }
}