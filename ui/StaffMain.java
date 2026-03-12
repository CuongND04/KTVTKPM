package ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StaffMain extends JFrame {
  private final JButton btnImportPart;

  public StaffMain() {
    setTitle("He thong quan ly gara oto");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 220);
    setLocationRelativeTo(null);

    JLabel title = new JLabel("Giao diện chính của nhân viên", SwingConstants.CENTER);
    title.setFont(new Font("Segoe UI", Font.BOLD, 24));

    btnImportPart = new JButton("Nhập linh kiện");
    btnImportPart.setFont(new Font("Segoe UI", Font.PLAIN, 16));

    JPanel centerPanel = new JPanel();
    centerPanel.add(btnImportPart);

    add(title, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
  }

  public JButton getBtnImportPart() {
    return btnImportPart;
  }
}