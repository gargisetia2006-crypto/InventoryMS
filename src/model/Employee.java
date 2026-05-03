package model;

public class Employee {
    private int id;
    private String name, email, phone, position, hireDate;
    private double salary;

    public Employee() {}
    public Employee(int id, String name, String email, String phone,
                    String position, double salary, String hireDate) {
        this.id = id; this.name = name; this.email = email;
        this.phone = phone; this.position = position;
        this.salary = salary; this.hireDate = hireDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    @Override
    public String toString() { return name; }
}
