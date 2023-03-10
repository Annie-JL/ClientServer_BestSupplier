package databaseManagement;

import models.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SQLOrder {
    private static SQLOrder instance;
    Connection myConnection;
    ArrayList<Order> orders;
    private java.sql.Statement stmt;
    PreparedStatement preparedStatement;

    private SQLOrder() {
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

    public static synchronized SQLOrder getInstance() {
        if (instance == null) {
            instance = new SQLOrder();
        }
        return instance;
    }

    public ArrayList<Order> get(String idUser){
        orders = new ArrayList<>();
        ResultSet resultSet;
        Order order;
        try {
            resultSet = stmt.executeQuery("SELECT orders.id, orders.score, orders.BestS, orders.date FROM orders INNER JOIN baskets ON baskets.idOrder = orders.id INNER JOIN users ON users.id = baskets.idAccount WHERE orders.id <> -1 AND users.id = " + idUser + " GROUP BY orders.id");

            while(resultSet.next()){
                order = new Order();
                order.setId(resultSet.getInt(1));
                order.setScore(resultSet.getFloat(2));
                order.setBestS(resultSet.getString(3));
                order.setDate(resultSet.getDate(4));
                orders.add(order);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных из бд!");
        }
        return orders;
    }

}
