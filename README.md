# 📋 Inventory Management System
**Pure Java | Swing GUI | SQLite Database | No external dependencies beyond JDBC**

---

## 📁 Project Structure

```
InventoryMS/
├── src/
│   ├── Main.java                    ← Entry point
│   ├── model/
│   │   ├── Product.java
│   │   ├── Customer.java
│   │   ├── Supplier.java
│   │   ├── Employee.java
│   │   ├── Invoice.java
│   │   └── InvoiceItem.java
│   ├── dao/                         ← Database Access Objects (CRUD logic)
│   │   ├── ProductDAO.java
│   │   ├── CustomerDAO.java
│   │   ├── SupplierDAO.java
│   │   ├── EmployeeDAO.java
│   │   └── InvoiceDAO.java
│   ├── ui/                          ← Swing UI panels
│   │   ├── MainFrame.java           ← Main window with sidebar
│   │   ├── BaseCRUDPanel.java       ← Reusable table+search+buttons base
│   │   ├── DashboardPanel.java
│   │   ├── SalesPanel.java
│   │   ├── ProductPanel.java
│   │   ├── CustomerPanel.java
│   │   ├── SupplierPanel.java
│   │   ├── EmployeePanel.java
│   │   ├── InvoicePanel.java
│   │   └── ReportsPanel.java
│   └── util/
│       ├── DatabaseConnection.java  ← SQLite connection + DB init
│       └── Theme.java               ← Dark theme colors & styles
├── lib/
│   └── sqlite-jdbc-3.45.3.0.jar    ← ⚠️ YOU MUST DOWNLOAD THIS
├── run.bat                          ← Windows build & run
├── run.sh                           ← Linux/Mac build & run
└── README.md
```

---

## ⚙️ Setup Steps

### Step 1 — Install Java JDK 17 or 21
Download from: https://adoptium.net/

### Step 2 — Download SQLite JDBC Driver
Download from: https://github.com/xerial/sqlite-jdbc/releases
- File: `sqlite-jdbc-3.45.3.0.jar`
- Place it in the `lib/` folder

### Step 3 — Open in NetBeans IDE 13
1. Open NetBeans → File → New Project → Java with Existing Sources
2. Point to the `InventoryMS/` folder
3. Add `src/` as source root
4. Right-click project → Properties → Libraries → Add JAR/Folder → select `lib/sqlite-jdbc-3.45.3.0.jar`
5. Set `Main` as the main class
6. Press Run ▶

### Step 4 — Run from Command Line (Alternative)
**Windows:**
```
run.bat
```
**Linux/Mac:**
```
chmod +x run.sh && ./run.sh
```

---

## 🗄️ Database
The app uses **SQLite** — a file-based database. On first run, it automatically creates `inventory.db` in the working directory. No MySQL, no PostgreSQL, no setup needed!

**Tables created automatically:**
- `products`
- `customers`
- `suppliers`
- `employees`
- `invoices`
- `invoice_items`
- `purchase_orders`
- `purchase_order_items`

---

## ✅ Features

| Module | Features |
|--------|----------|
| **Dashboard** | Stats cards (products, customers, revenue), Low stock alerts |
| **Sales** | Cart-based invoice creation, auto stock deduction, tax+discount |
| **Products** | Add/Edit/Delete, search, low stock tracking, cost vs selling price |
| **Customers** | Full CRUD with contact info |
| **Suppliers** | Full CRUD with contact person |
| **Employees** | Full CRUD with salary and hire date |
| **Invoices** | View all invoices, click to see itemized breakdown |
| **Reports** | Sales summary, revenue totals, inventory value, low stock report |

---

## 🎨 UI Features
- Dark theme throughout
- Sidebar navigation with toggle buttons
- Search bar on every module
- Confirmation dialogs before delete
- Stock validation during sales (can't sell more than available)
- Transaction-safe invoice saving (rolls back if error)

---

## 💡 Architecture
- **MVC-like pattern**: Models → DAOs → UI Panels
- **BaseCRUDPanel**: Reusable base class so all CRUD screens share the same table+search+button layout
- **DatabaseConnection**: Singleton SQLite connection, auto-creates all tables on startup
- **Theme**: Centralized color and font constants for consistent dark UI
