import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import action.StaffMainAction;
import db.StaffDB;
import model.Staff;
import ui.StaffMain;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Staff currentStaff = new StaffDB().getFirstStaff();
      if (currentStaff == null) {
        JOptionPane.showMessageDialog(null, "Khong tim thay nhan vien nao trong database.");
        return;
      }

      StaffMain view = new StaffMain();
      new StaffMainAction(view, currentStaff).onCreate();
      view.setVisible(true);
    });
  }
}