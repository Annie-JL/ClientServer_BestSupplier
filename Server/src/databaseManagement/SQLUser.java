package databaseManagement;



import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SQLUser {

    private static SQLUser instance;
    Connection myConnection;
    ArrayList<User> users;
    private java.sql.Statement stmt;
    PreparedStatement preparedStatement;

    private SQLUser() {
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



    public static synchronized SQLUser getInstance() {
        if (instance == null) {
            instance = new SQLUser();
        }
        return instance;
    }

    public ArrayList<User> getUsers(){
        users = new ArrayList<>();
        ResultSet resultSet;
        User user;
        try {
            resultSet = stmt.executeQuery("SELECT * FROM users");
            while(resultSet.next()){
                user = new User();
                user.setIdUser(resultSet.getInt(1));
                user.setFio(resultSet.getString(5));
                user.setPhoneNumber(resultSet.getString(6));
                user.setGender(resultSet.getString(7));
                user.setRole(resultSet.getString(4));
                user.setLogin(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                users.add(user);
            }
        }
        catch(Exception e){
            System.out.println(e + "! Проблемы с записью данных о пользлвателях из бд!");
        }
        return users;
    }

    public void addUsers(User user){

        String insert = "INSERT INTO users (id , login, password, role, fio, phoneNumber, gender) VALUES(?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(insert);
            preparedStatement.setInt(1, user.getIdUser());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getFio());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getGender());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String registration(User user){
        String result = "false";
        ResultSet resultSet = null;
        String select = "SELECT users.login FROM users WHERE users.login=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if(!resultSet.next()){
                result = "true";
                addUsers(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public String editUser(User user) {
        String result = "false";
        ResultSet resultSet = null;
        String select = "SELECT users.login FROM users WHERE users.login=? AND NOT users.id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setInt(2, user.getIdUser());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if(!resultSet.next()){
                result = "true";
                updateUser(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public void updateUser(User user) {
        try {
            String query = "UPDATE users SET login = ?, password = ?, role = ?, fio = ?, phoneNumber = ?, gender =? WHERE id = ?";
            preparedStatement = myConnection.prepareStatement(query);
            preparedStatement.setInt(7, user.getIdUser());
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.setString(4, user.getFio());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, user.getGender());
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex + "Проблема с изменением!");
        }
    }

    public User logIn(User user){
        String select = "SELECT * FROM users WHERE users.login= ? AND users.password = ?";
        ResultSet resultSet;
        try{
            PreparedStatement preparedStatement = myConnection.prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user.setIdUser(resultSet.getInt(1));
                user.setFio(resultSet.getString(5));
                user.setPhoneNumber(resultSet.getString(6));
                user.setGender(resultSet.getString(7));
                user.setRole(resultSet.getString(4));
            }
            else user.setRole("undefined");
        }
        catch (Exception ex) {
            System.out.println(ex + "Произошла ошибка при попытке входа!");
        }
        return user;
    }

    public void delete(int id){
        String insertStr = "DELETE FROM users WHERE id= ?";
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
