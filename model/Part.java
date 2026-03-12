package model;

public class Part {
  private int id;
  private String name;
  private String description;
  private int quantity;
  private float buyPrice;
  private float sellPrice;

  public Part() {
  }

  public Part(int id, String name, String description, int quantity, float buyPrice, float sellPrice) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.buyPrice = buyPrice;
    this.sellPrice = sellPrice;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public float getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(float buyPrice) {
    this.buyPrice = buyPrice;
  }

  public float getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(float sellPrice) {
    this.sellPrice = sellPrice;
  }

  @Override
  public String toString() {
    return name;
  }
}