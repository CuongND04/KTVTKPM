package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.table.DefaultTableModel;

import db.SupplierDB;
import model.Supplier;
import ui.SearchSupplier;

public class SearchSupplierAction implements ActionListener {
  private final SearchSupplier view;
  private final SupplierDB supplierDB = new SupplierDB();
  private final Consumer<Supplier> onSelected;

  public SearchSupplierAction(SearchSupplier view, Consumer<Supplier> onSelected) {
    this.view = view;
    this.onSelected = onSelected;
  }

  public void onCreate() {
    view.getBtnSearch().addActionListener(this);
    view.getBtnSelect().addActionListener(this);
    search();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == view.getBtnSearch())
      search();
    else if (source == view.getBtnSelect())
      selectSupplier();
  }

  private void search() {
    String keyword = view.getTxtKeyword().getText();
    ArrayList<Supplier> suppliers = supplierDB.searchSupplier(keyword);
    DefaultTableModel model = view.getTblModel();
    model.setRowCount(0);
    for (Supplier supplier : suppliers) {
      model.addRow(new Object[] {
          supplier.getId(),
          supplier.getName(),
          supplier.getAddress(),
          supplier.getPhone(),
          supplier.getEmail()
      });
    }
  }

  private void selectSupplier() {
    int row = view.getTblResult().getSelectedRow();
    if (row < 0)
      return;
    Supplier selected = new Supplier(
        (int) view.getTblModel().getValueAt(row, 0),
        String.valueOf(view.getTblModel().getValueAt(row, 1)),
        String.valueOf(view.getTblModel().getValueAt(row, 2)),
        String.valueOf(view.getTblModel().getValueAt(row, 3)),
        String.valueOf(view.getTblModel().getValueAt(row, 4)));
    onSelected.accept(selected);
    view.dispose();
  }
}