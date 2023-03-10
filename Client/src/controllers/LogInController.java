package controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInController implements IController{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login;

    @FXML
    private Button logInBtn;

    @FXML
    private Button registrationBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private PasswordField password;

    User user;

    @FXML
    void initialize() {
        exitBtn.setOnAction(actionEvent -> {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        registrationBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/registration.fxml", "Регистрация");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        logInBtn.setOnAction(actionEvent -> {
            try {
                logIn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Личный кабинет")) {
            UserMenuController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        } else if (title.equals("Администрирование")) {
            AdminMenuController controller = loader.getController();
            controller.action();
        } else if (title.equals("Регистрация")) {
            RegistrationController controller = loader.getController();
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void close() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText("Вы точно хотите выйти?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    public void logIn() throws IOException{
        String answer;
        if(!login.getText().isEmpty() || !password.getText().isEmpty()){
            Client.getInstance().sendMessage("logIn");
            user = new User();
            user.setLogin(login.getText());
            user.setPassword(password.getText());
            Client.getInstance().sendObject(user);
            answer = Client.getInstance().readMessage();
            if(answer.equals("admin")){
                user = (User)Client.getInstance().readObject();
                openWindow("/views/adminMenu.fxml", "Администрирование");
            }
            else if(answer.equals("user")){
                user = (User)Client.getInstance().readObject();
                openWindow("/views/userMenu.fxml", "Личный кабинет");
            }
            else if(answer.equals("undefined")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Логин или пароль введены неверно!");
                alert.showAndWait();
                login.setText("");
                password.setText("");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Данные введены некоректно!");
            alert.showAndWait();
        }
    }
}