package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SearchSupplier extends JDialog {
  private final JTextField txtKeyword;
  private final JButton btnSearch;
  private final JButton btnSelect;
  private final JTable tblResult;
  private final DefaultTableModel tblModel;

  public SearchSupplier(java.awt.Frame owner) {
    super(owner, "SearchSupplier", true);
    setSize(700, 420);
    setLocationRelativeTo(owner);

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(new JLabel("Tu khoa:"));
    txtKeyword = new JTextField(25);
    btnSearch = new JButton("Tim");
    topPanel.add(txtKeyword);
    topPanel.add(btnSearch);

    tblModel = new DefaultTableModel(new Object[] { "ID", "Ten NCC", "Dia chi", "SDT", "Email" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tblResult = new JTable(tblModel);

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnSelect = new JButton("Chon");
    bottomPanel.add(btnSelect);

    add(topPanel, BorderLayout.NORTH);
    add(new JScrollPane(tblResult), BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
  }

  public JTextField getTxtKeyword() {
    return txtKeyword;
  }

  public JButton getBtnSearch() {
    return btnSearch;
  }

  public JButton getBtnSelect() {
    return btnSelect;
  }

  public JTable getTblResult() {
    return tblResult;
  }

  public DefaultTableModel getTblModel() {
    return tblModel;
  }
}