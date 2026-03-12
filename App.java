import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import command.StaffMainCommand;
import db.StaffDB;
import model.Staff;
import ui.ImportInvoiceView;
import ui.StaffMainView;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Staff currentStaff = new StaffDB().getFirstStaff();
      if (currentStaff == null) {
        JOptionPane.showMessageDialog(null, "Khong tim thay nhan vien nao trong database.");
        return;
      }

      StaffMainView view = new StaffMainView();
      view.setOpenImportCmd(new StaffMainCommand(() -> new ImportInvoiceView(currentStaff).setVisible(true)));
      view.setVisible(true);
    });
  }
}