package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Basket;
import models.User;

public class EditInBasketController implements  IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label titleLabel;


    @FXML
    private TextField countField;



    User user;
    Basket basket;
    int max;
    String path;
    String title;

    public void setPath(String path, String title) {
        this.path = path;
        this.title = title;
    }


    @FXML
    void initialize() {}

    public void action() {
        cancelBtn.setOnAction(event -> {
            try {
                openWindow("/views/basket.fxml", "Выбранные поставщики");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        submitBtn.setOnAction(event -> {
            edit();
        });
    }

    private void edit() {
        boolean isCount = false;
        if(countField.getText().matches("^\\d{1,}$") && Integer.parseInt(countField.getText()) > 0 && Integer.parseInt(countField.getText()) <= max) {
            isCount = true;
        }

        if(!isCount) {
            countField.setText("");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Введена неверная оценка!");
            alert.showAndWait();
        }else {
            int tmp = basket.getCount();
            basket.setCount(Integer.parseInt(countField.getText()));
            Client.getInstance().sendMessage("basketEdit");
            Client.getInstance().sendObject(basket);
            String answer = null;
            try {
                answer = Client.getInstance().readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (answer.equals("true")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Оповещение");
                alert.setHeaderText("Запись успешно изменена!");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    try {
                        openWindow("/views/basket.fxml", "Выбранные поставщики");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
        if (title.equals("Корзина")) {
            BasketController controller = loader.getController();
            controller.setUser(user);
            controller.setPath(this.path, this.title);
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
        titleLabel.setText(titleLabel.getText() + " " + basket.getSupplierTitle());
        countField.setText(String.valueOf(basket.getCount()));
        max = basket.getCount();
    }
}

