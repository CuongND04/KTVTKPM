package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
  private static final String URL = "jdbc:mysql://localhost:3306/nhap_linh_kien?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
  private static final String USER = "root";
  private static final String PASSWORD = "Cuong178";

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.err.println("Khong tim thay MySQL JDBC Driver. Hay them mysql-connector-j vao classpath.");
    }
  }

  private DBHelper() {
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}