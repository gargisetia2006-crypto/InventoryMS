@echo off
REM ============================================================
REM  Inventory Management System - Build & Run Script (Windows)
REM ============================================================

SET JAVA_HOME=C:\Program Files\Java\jdk-21
SET JAR=lib\sqlite-jdbc-3.45.3.0.jar
SET SRC=src
SET BIN=bin
SET MAIN=Main

IF NOT EXIST %BIN% mkdir %BIN%

echo Compiling sources...
"%JAVA_HOME%\bin\javac" -cp "%JAR%" -d %BIN% ^
  %SRC%\util\DatabaseConnection.java ^
  %SRC%\util\Theme.java ^
  %SRC%\model\Product.java ^
  %SRC%\model\Customer.java ^
  %SRC%\model\Supplier.java ^
  %SRC%\model\Employee.java ^
  %SRC%\model\Invoice.java ^
  %SRC%\model\InvoiceItem.java ^
  %SRC%\dao\ProductDAO.java ^
  %SRC%\dao\CustomerDAO.java ^
  %SRC%\dao\SupplierDAO.java ^
  %SRC%\dao\EmployeeDAO.java ^
  %SRC%\dao\InvoiceDAO.java ^
  %SRC%\ui\BaseCRUDPanel.java ^
  %SRC%\ui\DashboardPanel.java ^
  %SRC%\ui\ProductPanel.java ^
  %SRC%\ui\CustomerPanel.java ^
  %SRC%\ui\SupplierPanel.java ^
  %SRC%\ui\EmployeePanel.java ^
  %SRC%\ui\SalesPanel.java ^
  %SRC%\ui\InvoicePanel.java ^
  %SRC%\ui\ReportsPanel.java ^
  %SRC%\ui\MainFrame.java ^
  %SRC%\Main.java

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running application...
"%JAVA_HOME%\bin\java" -cp "%BIN%;%JAR%" %MAIN%
pause
