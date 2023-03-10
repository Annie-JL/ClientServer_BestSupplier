package databaseManagement;


import models.Category;
import models.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLSuppliers {
    private static SQLSuppliers instance;
    Connection myConnection;
    ArrayList<Supplier> supplierArrayList;
    private java.sql.Statement stmt;
    PreparedStatement preparedStatement;

    private SQLSuppliers() {
        try {
            SQLConnection con = new SQLConnection();
            con.init();
            myConnection = con.getConnectionObj();
            stmt = myConnection.createStatement();
        }
        catch(Exception ex) {
            System.out.println(ex);
            System.out.println("Ошибка подключения к бд!");
        }
    }

    public static synchronized SQLSuppliers getInstance() {
        if (instance == null) {
            instance = new SQLSuppliers();
        }
        return instance;
    }

    public ArrayList<Supplier> get(){
        this.supplierArrayList = new ArrayList<>();
        ResultSet resultSet;
        Supplier supplier;
        try {
            resultSet = stmt.executeQuery("SELECT suppliers.id, suppliers.title, suppliers.description, categories.title FROM suppliers INNER JOIN  categories ON suppliers.idCategory = categories.id");
            while(resultSet.next()){
                supplier = new Supplier();
                supplier.setId(resultSet.getInt(1));
                supplier.setTitle(resultSet.getString(2));
                supplier.setDescription(resultSet.getString(3));
                supplier.setTitleCategory(resultSet.getString(4));
                this.supplierArrayList.add(supplier);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return this.supplierArrayList;
    }

    public void add(Supplier supplier){
        String insert = "INSERT INTO suppliers (idCategory, title, description) VALUES(?,?,?)";


        try {
            PreparedStatement preparedStatement = myConnection.prepareStatement(insert);
            preparedStatement.setInt(1, supplier.getIdCategory());
            preparedStatement.setString(2, supplier.getTitle());
            preparedStatement.setString(3, supplier.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void edit(Supplier supplier) {
        ResultSet resultSet;
        Category category = new Category();
        try {
            String select = "SELECT categories.id FROM categories WHERE categories.title = ?";
            PreparedStatement preparedStatement = myConnection.prepareStatement(select);
            preparedStatement.setString(1, supplier.getTitleCategory());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                category.setId(resultSet.getInt(1));
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }

        try {
            String query = "UPDATE suppliers SET idCategory = ?, title = ?, description = ? WHERE id = ?";
            preparedStatement = myConnection.prepareStatement(query);
            preparedStatement.setInt(4, supplier.getId());
            preparedStatement.setInt(1, category.getId());
            preparedStatement.setString(2, supplier.getTitle());
            preparedStatement.setString(3, supplier.getDescription());
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex + "Проблема с изменением!");
        }
    }

    public void delete(int id){
        String insertStr = "DELETE FROM suppliers WHERE id= ?";
        try {
            preparedStatement = myConnection.prepareStatement(insertStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<Supplier> searchTitle(String title) {
        this.supplierArrayList = new ArrayList<>();
        ResultSet resultSet;
        Supplier supplier;
        try {
            resultSet = stmt.executeQuery("SELECT suppliers.id, suppliers.title, suppliers.description, categories.title FROM suppliers INNER JOIN  categories ON suppliers.idCategory = categories.id WHERE suppliers.title LIKE '%"+ title+"%'");
            while(resultSet.next()){
                supplier = new Supplier();
                supplier.setId(resultSet.getInt(1));
                supplier.setTitle(resultSet.getString(2));
                supplier.setDescription(resultSet.getString(3));
                supplier.setTitleCategory(resultSet.getString(4));
                this.supplierArrayList.add(supplier);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return this.supplierArrayList;
    }

    public ArrayList<Supplier> searchCategory(String supplierCategory) {
            this.supplierArrayList = new ArrayList<>();
        ResultSet resultSet;
        Supplier supplier;
        try {
            resultSet = stmt.executeQuery("SELECT suppliers.id, suppliers.title, suppliers.description, categories.title FROM suppliers INNER JOIN  categories ON suppliers.idCategory = categories.id WHERE categories.title = '" + supplierCategory +"'");
            while(resultSet.next()){
                supplier = new Supplier();
                supplier.setId(resultSet.getInt(1));
                supplier.setTitle(resultSet.getString(2));
                supplier.setDescription(resultSet.getString(3));
                supplier.setTitleCategory(resultSet.getString(4));
                this.supplierArrayList.add(supplier);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return this.supplierArrayList;
    }
}
