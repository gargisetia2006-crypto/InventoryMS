#!/bin/bash
# ============================================================
#  Inventory Management System - Build & Run Script (Linux/Mac)
# ============================================================

JAR="lib/sqlite-jdbc-3.45.3.0.jar"
BIN="bin"
SRC="src"

mkdir -p $BIN

echo "Compiling sources..."
javac -cp "$JAR" -d "$BIN" \
  $SRC/util/DatabaseConnection.java \
  $SRC/util/Theme.java \
  $SRC/model/Product.java \
  $SRC/model/Customer.java \
  $SRC/model/Supplier.java \
  $SRC/model/Employee.java \
  $SRC/model/Invoice.java \
  $SRC/model/InvoiceItem.java \
  $SRC/dao/ProductDAO.java \
  $SRC/dao/CustomerDAO.java \
  $SRC/dao/SupplierDAO.java \
  $SRC/dao/EmployeeDAO.java \
  $SRC/dao/InvoiceDAO.java \
  $SRC/ui/BaseCRUDPanel.java \
  $SRC/ui/DashboardPanel.java \
  $SRC/ui/ProductPanel.java \
  $SRC/ui/CustomerPanel.java \
  $SRC/ui/SupplierPanel.java \
  $SRC/ui/EmployeePanel.java \
  $SRC/ui/SalesPanel.java \
  $SRC/ui/InvoicePanel.java \
  $SRC/ui/ReportsPanel.java \
  $SRC/ui/MainFrame.java \
  $SRC/Main.java

if [ $? -ne 0 ]; then
  echo "Compilation failed!"
  exit 1
fi

echo ""
echo "Running application..."
java -cp "$BIN:$JAR" Main
