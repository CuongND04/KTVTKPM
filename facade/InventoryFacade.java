package facade;

import java.time.LocalDate;
import java.util.ArrayList;

import db.ImportInvoiceDB;
import db.PartDB;
import db.SupplierDB;
import model.ImportDetail;
import model.ImportInvoice;
import model.Part;
import model.Staff;
import model.Supplier;

public class InventoryFacade {
  private final ImportInvoiceDB importInvoiceDB = new ImportInvoiceDB();
  private final PartDB partDB = new PartDB();
  private final SupplierDB supplierDB = new SupplierDB();

  public boolean processImportInvoice(Staff staff, Supplier supplier, ArrayList<ImportDetail> details) {
    if (staff == null || supplier == null || details == null || details.isEmpty())
      return false;

    ImportInvoice invoice = new ImportInvoice();
    invoice.setCreateDate(LocalDate.now());
    invoice.setCreateStaff(staff);
    invoice.setSupplier(supplier);
    invoice.setImportDetails(details);
    invoice.recalculateTotalAmount();
    return importInvoiceDB.addImportInvoice(invoice);
  }

  public ArrayList<Supplier> searchSupplier(String keyword) {
    return supplierDB.searchSupplier(keyword);
  }

  public ArrayList<Part> searchPart(String keyword) {
    return partDB.searchPart(keyword);
  }
}
