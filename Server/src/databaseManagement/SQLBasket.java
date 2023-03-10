package databaseManagement;

import models.Basket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class SQLBasket {
    private static SQLBasket instance;
    Connection myConnection;
    ArrayList<Basket> baskets;
    private java.sql.Statement stmt;
    PreparedStatement preparedStatement;
    Basket maxB;
    int max;
    String maxxx;

    private SQLBasket() {
        try {
            SQLConnection con = new SQLConnection();
            con.init();
            myConnection = con.getConnectionObj();
            stmt = myConnection.createStatement();
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Ошибка подключения к бд!");
        }
    }

    public static synchronized SQLBasket getInstance() {
        if (instance == null) {
            instance = new SQLBasket();
        }
        return instance;
    }

    public ArrayList<Basket> get(String idUser){
        baskets = new ArrayList<>();
        ResultSet resultSet;
        Basket basket;
        try {
            resultSet = stmt.executeQuery("SELECT baskets.id, baskets.idSupplier, baskets.idAccount, baskets.count, baskets.vision, suppliers.title FROM baskets INNER JOIN suppliers ON baskets.idSupplier = suppliers.id WHERE baskets.vision = 1 AND baskets.idAccount = " + idUser);

            while(resultSet.next()){
                basket = new Basket();
                basket.setId(resultSet.getInt(1));
                basket.setIdSupplier(resultSet.getInt(2));
                basket.setIdAccount(resultSet.getInt(3));
                basket.setCount(resultSet.getInt(4));
                basket.setVision(resultSet.getBoolean(5));
                basket.setSupplierTitle(resultSet.getString(6));
                baskets.add(basket);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return baskets;
    }

    public void add(Basket basket){

        String insert = "INSERT INTO baskets (id , idSupplier, idAccount, idOrder, vision, count) VALUES(?,?,?,?,?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(insert);
            preparedStatement.setInt(1, basket.getId());
            preparedStatement.setInt(2, basket.getIdSupplier());
            preparedStatement.setInt(3, basket.getIdAccount());
            preparedStatement.setInt(4,-1);
            preparedStatement.setBoolean(5, basket.getVision());
            preparedStatement.setInt(6, basket.getCount());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void findMax(ArrayList<Basket> baskets) {
        float max = 0;
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        for (Basket ba: baskets) {
            if (max<ba.getCount()){
                max = ba.getCount();
                maxB = ba;
            }
        }
        maxxx = maxB.getSupplierTitle();
        String insert = "INSERT INTO orders (score, BestS, date) VALUES(?, ?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(insert);
            preparedStatement.setFloat(1, max);
            preparedStatement.setString(2,maxxx);
            preparedStatement.setDate(3, date);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ResultSet resultSet;
        int id = 0;
        try {
            resultSet = stmt.executeQuery("SELECT orders.id FROM orders ORDER BY id DESC LIMIT 1");

            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }

        for (Basket ba : baskets) {
            ba.setIdOrder(id);
            ba.setVision(false);
            editOrder(ba);
        }

    }

    public void editOrder(Basket basket) {
        try {
            String query = "UPDATE baskets SET idOrder = ?, vision = ? WHERE id = ?";
            preparedStatement = myConnection.prepareStatement(query);
            preparedStatement.setInt(3, basket.getId());
            preparedStatement.setInt(1, basket.getIdOrder());
            preparedStatement.setBoolean(2, basket.getVision());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex + "Проблема с изменением!");
        }
    }

    public void edit(Basket basket) {
        try {
            String query = "UPDATE baskets SET idSupplier = ?, idAccount = ?, count = ?, vision = ? WHERE id = ?";
            preparedStatement = myConnection.prepareStatement(query);
            preparedStatement.setInt(5, basket.getId());
            preparedStatement.setInt(1, basket.getIdSupplier());
            preparedStatement.setInt(2, basket.getIdAccount());
            preparedStatement.setInt(3, basket.getCount());
            preparedStatement.setBoolean(4, basket.getVision());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex + "Проблема с изменением!");
        }
    }

    public void delete(int id) {
        String insertStr = "DELETE FROM baskets WHERE id= ?";
        try {
            preparedStatement = myConnection.prepareStatement(insertStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<Basket> bill(String idUserBill, String idOrderBill) {
        baskets = new ArrayList<>();
        ResultSet resultSet;
        Basket basket;
        try {
            resultSet = stmt.executeQuery("SELECT baskets.id, baskets.idSupplier, baskets.idAccount, baskets.count, baskets.vision, suppliers.title FROM baskets INNER JOIN suppliers ON baskets.idSupplier = suppliers.id WHERE baskets.vision = 0 AND baskets.idAccount = " + idUserBill + " AND baskets.idOrder = " + idOrderBill);

            while(resultSet.next()){
                basket = new Basket();
                basket.setId(resultSet.getInt(1));
                basket.setIdSupplier(resultSet.getInt(2));
                basket.setIdAccount(resultSet.getInt(3));
                basket.setCount(resultSet.getInt(4));
                basket.setVision(resultSet.getBoolean(5));
                basket.setSupplierTitle((resultSet.getString(6)));
                baskets.add(basket);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return baskets;
    }
}