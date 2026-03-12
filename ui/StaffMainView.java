package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import command.ICommand;

public class StaffMainView extends JFrame implements ActionListener {
  private final JButton btnImportPart;
  private ICommand openImportCmd;

  public StaffMainView() {
    setTitle("He thong quan ly gara oto");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 220);
    setLocationRelativeTo(null);

    JLabel title = new JLabel("Giao dien chinh cua nhan vien", SwingConstants.CENTER);
    title.setFont(new Font("Segoe UI", Font.BOLD, 24));

    btnImportPart = new JButton("Nhap linh kien");
    btnImportPart.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    btnImportPart.addActionListener(this);

    JPanel centerPanel = new JPanel();
    centerPanel.add(btnImportPart);

    add(title, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
  }

  public void setOpenImportCmd(ICommand openImportCmd) {
    this.openImportCmd = openImportCmd;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnImportPart && openImportCmd != null) {
      openImportCmd.execute();
    }
  }
}