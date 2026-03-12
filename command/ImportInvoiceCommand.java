package command;

import java.time.LocalDate;
import java.util.ArrayList;

import db.ImportInvoiceDB;
import model.ImportDetail;
import model.ImportInvoice;
import model.Part;
import model.Staff;
import model.Supplier;

public class ImportInvoiceCommand implements ICommand {
  private String supplierName;
  private String partName;
  private Supplier selectedSupplier;
  private final Staff currentStaff;
  private final ArrayList<ImportDetail> listDetails;
  private final ImportInvoiceDB receiver = new ImportInvoiceDB();
  private boolean success;

  public ImportInvoiceCommand(Staff currentStaff, Supplier selectedSupplier, ArrayList<ImportDetail> listDetails) {
    this.currentStaff = currentStaff;
    this.selectedSupplier = selectedSupplier;
    this.listDetails = listDetails;
    this.supplierName = selectedSupplier == null ? "" : selectedSupplier.getName();
  }

  public void setSelectedSupplier(Supplier supplier) {
    this.selectedSupplier = supplier;
    this.supplierName = supplier == null ? "" : supplier.getName();
  }

  public void setSelectedPart(Part part) {
    this.partName = part == null ? "" : part.getName();
  }

  @Override
  public void execute() {
    if (currentStaff == null || selectedSupplier == null || listDetails == null || listDetails.isEmpty()) {
      success = false;
      return;
    }

    ImportInvoice invoice = new ImportInvoice();
    invoice.setCreateDate(LocalDate.now());
    invoice.setCreateStaff(currentStaff);
    invoice.setSupplier(selectedSupplier);
    invoice.setImportDetails(listDetails);
    invoice.recalculateTotalAmount();
    success = receiver.addImportInvoice(invoice);
  }

  public boolean isSuccess() {
    return success;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public String getPartName() {
    return partName;
  }
}