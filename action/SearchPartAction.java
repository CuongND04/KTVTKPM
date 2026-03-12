package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.table.DefaultTableModel;

import db.PartDB;
import model.Part;
import ui.SearchPart;

public class SearchPartAction implements ActionListener {
  private final SearchPart view;
  private final PartDB partDB = new PartDB();
  private final Consumer<Part> onSelected;

  public SearchPartAction(SearchPart view, Consumer<Part> onSelected) {
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
      selectPart();
  }

  private void search() {
    String keyword = view.getTxtKeyword().getText();
    ArrayList<Part> parts = partDB.searchPart(keyword);
    DefaultTableModel model = view.getTblModel();
    model.setRowCount(0);
    for (Part part : parts) {
      model.addRow(new Object[] {
          part.getId(),
          part.getName(),
          part.getDescription(),
          part.getQuantity(),
          part.getBuyPrice(),
          part.getSellPrice()
      });
    }
  }

  private void selectPart() {
    int row = view.getTblResult().getSelectedRow();
    if (row < 0)
      return;
    Part selected = new Part(
        (int) view.getTblModel().getValueAt(row, 0),
        String.valueOf(view.getTblModel().getValueAt(row, 1)),
        String.valueOf(view.getTblModel().getValueAt(row, 2)),
        (int) view.getTblModel().getValueAt(row, 3),
        ((Number) view.getTblModel().getValueAt(row, 4)).floatValue(),
        ((Number) view.getTblModel().getValueAt(row, 5)).floatValue());
    onSelected.accept(selected);
    view.dispose();
  }
}