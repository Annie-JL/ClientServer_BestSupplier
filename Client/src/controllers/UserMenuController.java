package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.User;

public class UserMenuController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button shopBtn;

    @FXML
    private Button canselBtn;

    @FXML
    private Button ordersBtn;

    @FXML
    private Button basketBtn;

    User user;

    @FXML
    void initialize() {}

    public void setUser(User user) {
        this.user = user;
    }

    public void action() {

        canselBtn.setOnAction(actionEvent -> {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        shopBtn.setOnAction(actionEvent ->{
            try {
                openWindow("/views/findSup.fxml", "Система оценки поставщиков");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ordersBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/myOrders.fxml", "Журнал");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        basketBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/basket.fxml", "Выбранные поставщики");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) ordersBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Система оценки поставщиков")) {
            FindSupController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        } else if (title.equals("Выбранные поставщики")) {
            BasketController controller = loader.getController();
            controller.setUser(user);
            controller.setPath("/views/userMenu.fxml", "Личный кабинет");
            controller.action();
        } else if (title.equals("Журнал")) {
            MyOrdersController controller = loader.getController();
            controller.setUser(user);
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
            openWindow("/views/login.fxml", "Вход");
        }
    }
}
