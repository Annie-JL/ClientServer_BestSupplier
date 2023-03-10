package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;

public class UserAddController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private PasswordField password;

    @FXML
    private TextField fio;

    @FXML
    private TextField phoneNumber;

    @FXML
    private ComboBox<String> gender;

    @FXML
    private ComboBox<String> role;

    @FXML
    void initialize() {}
     public void action() {
         fillData();

         submitBtn.setOnAction(actionEvent -> {
             try {
                 submit();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         });
         cancelBtn.setOnAction(actionEvent -> {
             try {
                 openWindow("/views/usersManager.fxml", "Управление пользователями");
             } catch (IOException e) {
                 e.printStackTrace();
             }
         });
     }

    private void fillData() {
        gender.getItems().addAll("Не скажу","Мужчина", "Женщина");
        role.getItems().addAll("Администратор","Пользователь");
    }

    @Override
    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Управление пользователями")) {
            UsersManagerController controller = loader.getController();
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void submit() throws IOException {
        boolean isLogin = false, isPassword = false, isPhoneNumber = false, isFio = false, isGender = false, isRole=false;

        if(login.getText().matches("[a-zA-Z\\d]{4,20}")) {
            isLogin = true;
        }

        if(password.getText().matches("[\\S]{4,20}")) {
            isPassword = true;
        }

        if(fio.getText().matches("[а-яА-я\\s]{5,45}")) {
            isFio = true;
        }

        if(phoneNumber.getText().matches("^((\\+375)[\\- ]?)?\\(?\\d{2}\\)?[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}(([\\- ]?\\d{1})?[\\- ]?\\d{1})?$")) {
            isPhoneNumber = true;
        }

        if(gender.getValue() != null) {
            isGender = true;
        }

        if(role.getValue() != null) {
            isRole = true;
        }

        if(isLogin && isFio && isPassword && isPhoneNumber && isGender && isRole){
            User user = new User();
            user.setLogin(login.getText());
            user.setPassword(password.getText());
            if(role.getValue() == "Пользователь"){
                user.setRole("user");

            } else if(role.getValue()=="Администратор") {
                user.setRole("admin");

            }
            user.setFio(fio.getText());
            user.setPhoneNumber(phoneNumber.getText());
            user.setGender(gender.getValue());
            Client.getInstance().sendMessage("registration");
            Client.getInstance().sendObject(user);
            String answer = Client.getInstance().readMessage();
            if (answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Оповещение");
                alert.setHeaderText("Пользователь успешно зарегистрирован!");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    openWindow("/views/usersManager.fxml", "Управление пользователями");
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Пользователь с таким логином уже существует");
                alert.showAndWait();
                isLogin = false;
                login.setText("");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Данные введены некорректно!");
            alert.showAndWait();
            if(!isLogin) {
                login.setText("");
            }
            if(!isFio) {
                fio.setText("");
            }
            if(!isPhoneNumber){
                phoneNumber.setText("");
            }
            if(!isPassword){
                password.setText("");
            }
        }
    }

}
