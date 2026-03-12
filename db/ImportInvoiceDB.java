package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ImportDetail;
import model.ImportInvoice;
import model.Part;
import model.Staff;
import model.Supplier;

public class ImportInvoiceDB {
  public boolean addImportInvoice(ImportInvoice invoice) {
    if (invoice == null || invoice.getSupplier() == null || invoice.getCreateStaff() == null)
      return false;

    List<ImportDetail> details = invoice.getImportDetails();
    if (details == null || details.isEmpty())
      return false;

    for (ImportDetail detail : details) {
      if (detail == null || detail.getPart() == null || detail.getQuantity() <= 0)
        return false;
      detail.recalculateTotal();
    }

    if (invoice.getCreateDate() == null)
      return false;

    String insertInvoiceSql = """
        INSERT INTO import_invoice (create_date, total_amount, supplier_id, create_staff_id)
        VALUES (?, ?, ?, ?)
        """;

    String insertDetailSql = """
        INSERT INTO import_detail (invoice_id, part_id, quantity, price, total_amount)
        VALUES (?, ?, ?, ?, ?)
        """;

    String updatePartQuantitySql = """
        UPDATE part
        SET quantity = quantity + ?
        WHERE id = ?
        """;

    try (Connection conn = DBHelper.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement psInvoice = conn.prepareStatement(insertInvoiceSql, Statement.RETURN_GENERATED_KEYS);
          PreparedStatement psDetail = conn.prepareStatement(insertDetailSql);
          PreparedStatement psUpdatePart = conn.prepareStatement(updatePartQuantitySql)) {

        psInvoice.setDate(1, Date.valueOf(invoice.getCreateDate()));
        psInvoice.setFloat(2, invoice.getTotalAmount());
        psInvoice.setInt(3, invoice.getSupplier().getId());
        psInvoice.setInt(4, invoice.getCreateStaff().getId());
        psInvoice.executeUpdate();

        try (ResultSet rs = psInvoice.getGeneratedKeys()) {
          if (!rs.next()) {
            conn.rollback();
            return false;
          }
          invoice.setId(rs.getInt(1));
        }

        for (ImportDetail detail : details) {
          psDetail.setInt(1, invoice.getId());
          psDetail.setInt(2, detail.getPart().getId());
          psDetail.setInt(3, detail.getQuantity());
          psDetail.setFloat(4, detail.getPrice());
          psDetail.setFloat(5, detail.getTotalAmount());
          psDetail.executeUpdate();

          psUpdatePart.setInt(1, detail.getQuantity());
          psUpdatePart.setInt(2, detail.getPart().getId());
          psUpdatePart.executeUpdate();
        }

        conn.commit();
        logCreatedInvoice(invoice);
        return true;
      } catch (SQLException e) {
        conn.rollback();
        e.printStackTrace();
        return false;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<ImportInvoice> getAll() {
    List<ImportInvoice> invoices = new ArrayList<>();

    String sql = """
        SELECT i.id, i.create_date, i.total_amount,
               s.id AS supplier_id, s.name AS supplier_name, s.address AS supplier_address,
               s.phone AS supplier_phone, s.email AS supplier_email,
               st.id AS staff_id, st.name AS staff_name, st.username AS staff_username,
               st.password AS staff_password, st.email AS staff_email, st.role AS staff_role
        FROM import_invoice i
        JOIN supplier s ON i.supplier_id = s.id
        JOIN staff st ON i.create_staff_id = st.id
        ORDER BY i.id DESC
        """;

    try (Connection conn = DBHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        ImportInvoice invoice = new ImportInvoice();
        invoice.setId(rs.getInt("id"));
        invoice.setCreateDate(rs.getDate("create_date").toLocalDate());
        invoice.setTotalAmount(rs.getFloat("total_amount"));

        Supplier supplier = new Supplier(
            rs.getInt("supplier_id"),
            rs.getString("supplier_name"),
            rs.getString("supplier_address"),
            rs.getString("supplier_phone"),
            rs.getString("supplier_email"));
        invoice.setSupplier(supplier);

        Staff staff = new Staff(
            rs.getInt("staff_id"),
            rs.getString("staff_name"),
            rs.getString("staff_username"),
            rs.getString("staff_password"),
            rs.getString("staff_email"),
            rs.getString("staff_role"));
        invoice.setCreateStaff(staff);

        invoice.setImportDetails(loadDetailsByInvoiceId(conn, invoice.getId()));
        invoices.add(invoice);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return invoices;
  }

  private List<ImportDetail> loadDetailsByInvoiceId(Connection conn, int invoiceId) throws SQLException {
    List<ImportDetail> details = new ArrayList<>();

    String sql = """
        SELECT d.id, d.quantity, d.price, d.total_amount,
               p.id AS part_id, p.name AS part_name, p.description AS part_description,
               p.quantity AS part_quantity, p.buy_price, p.sell_price
        FROM import_detail d
        JOIN part p ON d.part_id = p.id
        WHERE d.invoice_id = ?
        ORDER BY d.id
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, invoiceId);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Part part = new Part(
              rs.getInt("part_id"),
              rs.getString("part_name"),
              rs.getString("part_description"),
              rs.getInt("part_quantity"),
              rs.getFloat("buy_price"),
              rs.getFloat("sell_price"));

          ImportDetail detail = new ImportDetail(
              rs.getInt("id"),
              rs.getInt("quantity"),
              rs.getFloat("price"),
              part);
          detail.setTotalAmount(rs.getFloat("total_amount"));
          details.add(detail);
        }
      }
    }

    return details;
  }

  private void logCreatedInvoice(ImportInvoice invoice) {
    Supplier supplier = invoice.getSupplier();
    Staff staff = invoice.getCreateStaff();

    StringBuilder sb = new StringBuilder();
    sb.append("\n===== PHIEU NHAP MOI =====\n");
    sb.append("ID: ").append(invoice.getId()).append('\n');
    sb.append("Ngay tao: ").append(invoice.getCreateDate()).append('\n');
    sb.append("Nhan vien: ").append(staff == null ? "N/A" : staff.getName()).append('\n');
    sb.append("Nha cung cap: ").append(supplier == null ? "N/A" : supplier.getName()).append('\n');
    sb.append("Tong tien: ").append(invoice.getTotalAmount()).append('\n');
    sb.append("Chi tiet:\n");

    List<ImportDetail> details = invoice.getImportDetails();
    if (details == null || details.isEmpty()) {
      sb.append("- Khong co dong chi tiet\n");
    } else {
      int index = 1;
      for (ImportDetail detail : details) {
        Part part = detail.getPart();
        sb.append(index++)
            .append(". ")
            .append(part == null ? "N/A" : part.getName())
            .append(" | SL=").append(detail.getQuantity())
            .append(" | Gia=").append(detail.getPrice())
            .append(" | Thanh tien=").append(detail.getTotalAmount())
            .append('\n');
      }
    }

    sb.append("==========================\n");
    System.out.print(sb.toString());
  }
}