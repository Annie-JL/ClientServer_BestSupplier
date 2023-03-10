package databaseManagement;

import models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLCategory {
    private static SQLCategory instance;
    Connection myConnection;
    ArrayList<Category> categories;
    private java.sql.Statement stmt;
    PreparedStatement preparedStatement;

    private SQLCategory() {
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

    public static synchronized SQLCategory getInstance() {
        if (instance == null) {
            instance = new SQLCategory();
        }
        return instance;
    }

    public ArrayList<Category> get(){
        categories = new ArrayList<>();
        ResultSet resultSet;
        Category category;
        try {
            resultSet = stmt.executeQuery("SELECT * FROM categories");
            while(resultSet.next()){
                category = new Category();
                category.setId(resultSet.getInt(1));
                category.setTitle(resultSet.getString(2));
                categories.add(category);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return categories;
    }

    public void add(Category category){

        String insert = "INSERT INTO categories (id , title) VALUES(?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(insert);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.setString(2, category.getTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void edit(Category category) {
        try {
            String query = "UPDATE categories SET title = ?  WHERE id = ?";
            preparedStatement = myConnection.prepareStatement(query);
            preparedStatement.setInt(2, category.getId());
            preparedStatement.setString(1, category.getTitle());
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex + "Проблема с изменением!");
        }
    }

    public void delete(int id){
        String insertStr = "DELETE FROM categories WHERE id= ?";
        try {
            preparedStatement = myConnection.prepareStatement(insertStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
