package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import facade.InventoryFacade;
import model.ImportDetail;
import model.Part;
import model.Staff;
import model.Supplier;
import ui.ImportInvoice;
import ui.SearchPart;
import ui.SearchSupplier;

public class ImportInvoiceAction implements ActionListener {
  private final ImportInvoice view;
  private final Staff currentStaff;
  private final InventoryFacade facade = new InventoryFacade();

  private Supplier selectedSupplier;
  private Part selectedPart;
  private List<ImportDetail> details = new ArrayList<>();

  public ImportInvoiceAction(ImportInvoice view, Staff currentStaff) {
    this.view = view;
    this.currentStaff = currentStaff;
  }

  public void onCreate() {
    view.getBtnSearchSupplier().addActionListener(this);
    view.getBtnSearchPart().addActionListener(this);
    view.getBtnAddPart().addActionListener(this);
    view.getBtnSave().addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == view.getBtnSearchSupplier())
      openSearchSupplier();
    else if (source == view.getBtnSearchPart())
      openSearchPart();
    else if (source == view.getBtnAddPart())
      addDetail();
    else if (source == view.getBtnSave())
      save();
  }

  public void setSelectedSupplier(Supplier supplier) {
    selectedSupplier = supplier;
    view.getTxtSupplierName().setText(supplier.getName());
  }

  public void setSelectedPart(Part part) {
    selectedPart = part;
    view.getTxtPartName().setText(part.getName());
    view.getTxtPrice().setText(String.valueOf(part.getBuyPrice()));
  }

  private void openSearchSupplier() {
    SearchSupplier dialog = new SearchSupplier(view);
    new SearchSupplierAction(dialog, this::setSelectedSupplier).onCreate();
    dialog.setVisible(true);
  }

  private void openSearchPart() {
    SearchPart dialog = new SearchPart(view);
    new SearchPartAction(dialog, this::setSelectedPart).onCreate();
    dialog.setVisible(true);
  }

  private void addDetail() {
    if (selectedPart == null)
      return;

    String qRaw = view.getTxtQuantity().getText().trim();
    String pRaw = view.getTxtPrice().getText().trim();
    int quantity = qRaw.isEmpty() ? 1 : Integer.parseInt(qRaw);
    float price = pRaw.isEmpty() ? 0 : Float.parseFloat(pRaw);
    if (quantity <= 0)
      quantity = 1;

    ImportDetail existed = findByPartId(selectedPart.getId());
    if (existed == null)
      details.add(new ImportDetail(0, quantity, price, selectedPart));
    else {
      existed.setQuantity(existed.getQuantity() + quantity);
      existed.setPrice(price);
    }

    refreshTable();
    view.getTxtQuantity().setText("");
    view.getTxtPartName().setText("");
    selectedPart = null;
  }

  private ImportDetail findByPartId(int partId) {
    for (ImportDetail d : details)
      if (d.getPart().getId() == partId)
        return d;
    return null;
  }

  private void refreshTable() {
    view.getTblModel().setRowCount(0);
    float total = 0;
    for (ImportDetail d : details) {
      d.recalculateTotal();
      total += d.getTotalAmount();
      view.getTblModel().addRow(new Object[] {
          d.getPart().getId(), d.getPart().getName(), d.getQuantity(), d.getPrice(), d.getTotalAmount()
      });
    }
    view.getTxtTotal().setText(String.valueOf(total));
  }

  private void save() {
    if (selectedSupplier == null || details.isEmpty())
      return;

    if (facade.processImportInvoice(currentStaff, selectedSupplier, new ArrayList<>(details))) {
      JOptionPane.showMessageDialog(view, "Luu phieu nhap thanh cong.");
      reset();
    } else {
      JOptionPane.showMessageDialog(view, "Luu phieu nhap that bai.");
    }
  }

  private void reset() {
    selectedSupplier = null;
    selectedPart = null;
    details = new ArrayList<>();
    view.getTxtSupplierName().setText("");
    view.getTxtPartName().setText("");
    view.getTxtQuantity().setText("");
    view.getTxtPrice().setText("");
    view.getTxtTotal().setText("0");
    view.getTblModel().setRowCount(0);
  }
}
