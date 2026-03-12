package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportInvoice {
  private int id;
  private LocalDate createDate;
  private float totalAmount;
  private Supplier supplier;
  private Staff createStaff;
  private List<ImportDetail> importDetails;

  public ImportInvoice() {
    this.importDetails = new ArrayList<>();
    this.createDate = LocalDate.now();
  }

  public ImportInvoice(int id, LocalDate createDate, float totalAmount, Supplier supplier, Staff createStaff,
      List<ImportDetail> importDetails) {
    this.id = id;
    this.createDate = createDate;
    this.totalAmount = totalAmount;
    this.supplier = supplier;
    this.createStaff = createStaff;
    this.importDetails = importDetails != null ? importDetails : new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }

  public float getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(float totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public Staff getCreateStaff() {
    return createStaff;
  }

  public void setCreateStaff(Staff createStaff) {
    this.createStaff = createStaff;
  }

  public List<ImportDetail> getImportDetails() {
    return importDetails;
  }

  public void setImportDetails(List<ImportDetail> importDetails) {
    this.importDetails = importDetails;
  }

  public void recalculateTotalAmount() {
    float total = 0;
    for (ImportDetail detail : importDetails) {
      total += detail.getTotalAmount();
    }
    this.totalAmount = total;
  }
}