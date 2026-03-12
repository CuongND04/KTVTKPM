package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Staff;
import ui.ImportInvoice;
import ui.StaffMain;

public class StaffMainAction implements ActionListener {
  private final StaffMain view;
  private final Staff currentStaff;

  public StaffMainAction(StaffMain view, Staff currentStaff) {
    this.view = view;
    this.currentStaff = currentStaff;
  }

  public void onCreate() {
    view.getBtnImportPart().addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == view.getBtnImportPart()) {
      ImportInvoice importInvoiceView = new ImportInvoice();
      new ImportInvoiceAction(importInvoiceView, currentStaff).onCreate();
      importInvoiceView.setVisible(true);
    }
  }
}