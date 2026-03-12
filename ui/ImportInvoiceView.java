package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import command.ICommand;
import command.ImportInvoiceCommand;
import model.ImportDetail;
import model.Part;
import model.Staff;
import model.Supplier;

public class ImportInvoiceView extends JFrame implements ActionListener {
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

  private final Staff currentStaff;
  private Supplier selectedSupplier;
  private Part selectedPart;
  private List<ImportDetail> listDetails = new ArrayList<>();

  private ICommand searchSupplierCmd;
  private ICommand searchPartCmd;
  private ICommand saveCmd;

  public ImportInvoiceView(Staff currentStaff) {
    this.currentStaff = currentStaff;

    setTitle("ImportInvoiceView");
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

    btnSearchSupplier.addActionListener(this);
    btnSearchPart.addActionListener(this);
    btnAddPart.addActionListener(this);
    btnSave.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == btnSearchSupplier) {
      openSearchSupplier();
    } else if (source == btnSearchPart) {
      openSearchPart();
    } else if (source == btnAddPart) {
      addDetail();
    } else if (source == btnSave) {
      save();
    }
  }

  public void setSelectedSupplier(Supplier supplier) {
    selectedSupplier = supplier;
    txtSupplierName.setText(supplier.getName());
  }

  public void setSelectedPart(Part part) {
    selectedPart = part;
    txtPartName.setText(part.getName());
    txtPrice.setText(String.valueOf(part.getBuyPrice()));
  }

  private void openSearchSupplier() {
    SearchSupplierView dialog = new SearchSupplierView(this, this::setSelectedSupplier);
    searchSupplierCmd = () -> dialog.setVisible(true);
    searchSupplierCmd.execute();
  }

  private void openSearchPart() {
    SearchPartView dialog = new SearchPartView(this, this::setSelectedPart);
    searchPartCmd = () -> dialog.setVisible(true);
    searchPartCmd.execute();
  }

  private void addDetail() {
    if (selectedPart == null)
      return;

    String qRaw = txtQuantity.getText().trim();
    String pRaw = txtPrice.getText().trim();
    int quantity = qRaw.isEmpty() ? 1 : Integer.parseInt(qRaw);
    float price = pRaw.isEmpty() ? 0 : Float.parseFloat(pRaw);
    if (quantity <= 0)
      quantity = 1;

    ImportDetail existed = findByPartId(selectedPart.getId());
    if (existed == null)
      listDetails.add(new ImportDetail(0, quantity, price, selectedPart));
    else {
      existed.setQuantity(existed.getQuantity() + quantity);
      existed.setPrice(price);
    }

    refreshTable();
    txtQuantity.setText("");
    txtPartName.setText("");
    selectedPart = null;
  }

  private ImportDetail findByPartId(int partId) {
    for (ImportDetail d : listDetails)
      if (d.getPart().getId() == partId)
        return d;
    return null;
  }

  private void refreshTable() {
    tblModel.setRowCount(0);
    float total = 0;
    for (ImportDetail d : listDetails) {
      d.recalculateTotal();
      total += d.getTotalAmount();
      tblModel.addRow(new Object[] {
          d.getPart().getId(), d.getPart().getName(), d.getQuantity(), d.getPrice(), d.getTotalAmount()
      });
    }
    txtTotal.setText(String.valueOf(total));
  }

  private void save() {
    if (selectedSupplier == null || listDetails.isEmpty())
      return;

    ImportInvoiceCommand command = new ImportInvoiceCommand(currentStaff, selectedSupplier,
        new ArrayList<>(listDetails));
    saveCmd = command;
    saveCmd.execute();

    if (command.isSuccess()) {
      JOptionPane.showMessageDialog(this, "Luu phieu nhap thanh cong.");
      reset();
    } else {
      JOptionPane.showMessageDialog(this, "Luu phieu nhap that bai.");
    }
  }

  private void reset() {
    selectedSupplier = null;
    selectedPart = null;
    listDetails = new ArrayList<>();
    txtSupplierName.setText("");
    txtPartName.setText("");
    txtQuantity.setText("");
    txtPrice.setText("");
    txtTotal.setText("0");
    tblModel.setRowCount(0);
  }
}