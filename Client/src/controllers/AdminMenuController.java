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

public class AdminMenuController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button usersBtn;

    @FXML
    private Button canselBtn;

    @FXML
    private Button analiticBtn;

    @FXML
    private Button categoriesBtn;

    @FXML
    private Button productsBtn;


    @FXML
    void initialize() { }

    public void action() {

        canselBtn.setOnAction(actionEvent -> {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        usersBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/usersManager.fxml", "Управление пользователями");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        categoriesBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/categoriesManager.fxml", "Управление категориями");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        productsBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/suppliersManager.fxml", "Управление поставщиками");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    @Override
    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) canselBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Управление пользователями")) {
            UsersManagerController controller = loader.getController();
            controller.action();
        } else if (title.equals("Управление категориями")) {
            CategoriesManagerController controller = loader.getController();
            controller.action();
        }
        else if (title.equals("Управление поставщиками")) {
            SuppliersManagerController controller = loader.getController();
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
