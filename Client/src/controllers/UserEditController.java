package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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

public class UserEditController implements IController {

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

    User user;

    public void fillData(User us){
        user = us;
        login.setText(user.getLogin());
        password.setText(user.getPassword());
        fio.setText(user.getFio());
        phoneNumber.setText(user.getPhoneNumber());
        gender.getItems().addAll("Не скажу","Мужчина", "Женщина");
        role.getItems().addAll("Администратор","Пользователь");
        gender.setValue(user.getGender());
        role.setValue(user.getRole());

        if(user.getRole().equals("user")){
            role.setValue("Пользователь");
        } else if(user.getRole().equals("admin")) {
            role.setValue("Администратор");
        }
    }

    @FXML
    void initialize() {}
    public void action(){
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
            User userNew = new User();
            userNew.setIdUser(user.getIdUser());
            userNew.setLogin(login.getText());
            userNew.setPassword(password.getText());
            if(role.getValue() == "Пользователь"){
                user.setRole("user");

            } else if(role.getValue()=="Администратор") {
                user.setRole("admin");

            }
            userNew.setFio(fio.getText());
            userNew.setPhoneNumber(phoneNumber.getText());
            userNew.setGender(gender.getValue());
            Client.getInstance().sendMessage("editUser");
            Client.getInstance().sendObject(userNew);
            String answer = Client.getInstance().readMessage();
            if (answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Оповещение");
                alert.setHeaderText("Данные успешно изменены!");
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
}
