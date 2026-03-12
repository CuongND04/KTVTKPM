package model;

public class ImportDetail {
  private int id;
  private int quantity;
  private float totalAmount;
  private float price;
  private Part part;

  public ImportDetail() {
  }

  public ImportDetail(int id, int quantity, float price, Part part) {
    this.id = id;
    this.quantity = quantity;
    this.price = price;
    this.part = part;
    this.totalAmount = quantity * price;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
    recalculateTotal();
  }

  public float getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(float totalAmount) {
    this.totalAmount = totalAmount;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
    recalculateTotal();
  }

  public Part getPart() {
    return part;
  }

  public void setPart(Part part) {
    this.part = part;
  }

  public void recalculateTotal() {
    this.totalAmount = this.quantity * this.price;
  }
}