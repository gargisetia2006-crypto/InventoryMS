package model;

public class Supplier {
    private int id;
    private String name, contactPerson, email, phone, address, city;

    public Supplier() {}
    public Supplier(int id, String name, String contactPerson, String email,
                    String phone, String address, String city) {
        this.id = id; this.name = name; this.contactPerson = contactPerson;
        this.email = email; this.phone = phone;
        this.address = address; this.city = city;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public String toString() { return name; }
}
