package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ImportInvoice extends JFrame {
  private final JTextField txtSupplierName;
  private final JTextField txtPartName;
  private final JTextField txtQuantity;
  private final JTextField txtPrice;
  private final JTextField txtTotal;
  private final JButton btnSearchSupplier;
  private final JButton btnSearchPart;
  private final JButton btnAddPart;
  private final JButton btnSave;
  private final JTable tblDetails;
  private final DefaultTableModel tblModel;

  public ImportInvoice() {
    setTitle("ImportInvoice");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);

    JPanel header = new JPanel(new GridLayout(3, 1, 5, 5));

    JPanel supplierPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    supplierPanel.add(new JLabel("Nha cung cap:"));
    txtSupplierName = new JTextField(28);
    txtSupplierName.setEditable(false);
    btnSearchSupplier = new JButton("Tim NCC");
    supplierPanel.add(txtSupplierName);
    supplierPanel.add(btnSearchSupplier);

    JPanel partPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    partPanel.add(new JLabel("Linh kien:"));
    txtPartName = new JTextField(20);
    txtPartName.setEditable(false);
    btnSearchPart = new JButton("Tim linh kien");
    partPanel.add(txtPartName);
    partPanel.add(btnSearchPart);

    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    inputPanel.add(new JLabel("So luong:"));
    txtQuantity = new JTextField(8);
    inputPanel.add(txtQuantity);
    inputPanel.add(new JLabel("Gia nhap:"));
    txtPrice = new JTextField(10);
    inputPanel.add(txtPrice);
    btnAddPart = new JButton("Them vao phieu");
    inputPanel.add(btnAddPart);

    header.add(supplierPanel);
    header.add(partPanel);
    header.add(inputPanel);

    tblModel = new DefaultTableModel(new Object[] { "Part ID", "Ten linh kien", "So luong", "Gia", "Thanh tien" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tblDetails = new JTable(tblModel);

    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    footer.add(new JLabel("Tong tien:"));
    txtTotal = new JTextField(15);
    txtTotal.setEditable(false);
    txtTotal.setText("0");
    btnSave = new JButton("Luu phieu nhap");
    footer.add(txtTotal);
    footer.add(btnSave);

    add(header, BorderLayout.NORTH);
    add(new JScrollPane(tblDetails), BorderLayout.CENTER);
    add(footer, BorderLayout.SOUTH);
  }

  public JTextField getTxtSupplierName() {
    return txtSupplierName;
  }

  public JTextField getTxtPartName() {
    return txtPartName;
  }

  public JTextField getTxtQuantity() {
    return txtQuantity;
  }

  public JTextField getTxtPrice() {
    return txtPrice;
  }

  public JTextField getTxtTotal() {
    return txtTotal;
  }

  public JButton getBtnSearchSupplier() {
    return btnSearchSupplier;
  }

  public JButton getBtnSearchPart() {
    return btnSearchPart;
  }

  public JButton getBtnAddPart() {
    return btnAddPart;
  }

  public JButton getBtnSave() {
    return btnSave;
  }

  public JTable getTblDetails() {
    return tblDetails;
  }

  public DefaultTableModel getTblModel() {
    return tblModel;
  }

  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }
}