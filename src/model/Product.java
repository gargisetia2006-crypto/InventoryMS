package model;

public class Product {
    private int id;
    private String name, category, description, unit;
    private double price, costPrice;
    private int quantity, minStock;

    public Product() {}

    public Product(int id, String name, String category, String description,
                   double price, double costPrice, int quantity, int minStock, String unit) {
        this.id = id; this.name = name; this.category = category;
        this.description = description; this.price = price;
        this.costPrice = costPrice; this.quantity = quantity;
        this.minStock = minStock; this.unit = unit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getCostPrice() { return costPrice; }
    public void setCostPrice(double costPrice) { this.costPrice = costPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getMinStock() { return minStock; }
    public void setMinStock(int minStock) { this.minStock = minStock; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    @Override
    public String toString() { return name; }
}
