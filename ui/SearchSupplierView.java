package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import command.ICommand;
import command.SearchSupplierCommand;
import model.Supplier;

public class SearchSupplierView extends JDialog implements ActionListener {
  private final JTextField txtKeyword;
  private final JButton btnSearch;
  private final JButton btnSelect;
  private final JTable tblResult;
  private final DefaultTableModel tblModel;

  private ArrayList<Supplier> resultList = new ArrayList<>();
  private ICommand searchCommand;
  private final Consumer<Supplier> onSelected;

  public SearchSupplierView(java.awt.Frame owner, Consumer<Supplier> onSelected) {
    super(owner, "SearchSupplierView", true);
    this.onSelected = onSelected;
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

    btnSearch.addActionListener(this);
    btnSelect.addActionListener(this);
    runSearch();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnSearch) {
      runSearch();
    } else if (e.getSource() == btnSelect) {
      selectSupplier();
    }
  }

  private void runSearch() {
    searchCommand = new SearchSupplierCommand(txtKeyword.getText());
    searchCommand.execute();
    resultList = ((SearchSupplierCommand) searchCommand).getResultList();

    tblModel.setRowCount(0);
    for (Supplier supplier : resultList) {
      tblModel.addRow(new Object[] {
          supplier.getId(), supplier.getName(), supplier.getAddress(), supplier.getPhone(), supplier.getEmail()
      });
    }
  }

  private void selectSupplier() {
    int row = tblResult.getSelectedRow();
    if (row < 0)
      return;
    Supplier selected = new Supplier(
        (int) tblModel.getValueAt(row, 0),
        String.valueOf(tblModel.getValueAt(row, 1)),
        String.valueOf(tblModel.getValueAt(row, 2)),
        String.valueOf(tblModel.getValueAt(row, 3)),
        String.valueOf(tblModel.getValueAt(row, 4)));
    if (onSelected != null) {
      onSelected.accept(selected);
    }
    dispose();
  }
}