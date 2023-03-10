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
import models.Supplier;
import models.User;


public class AddInBasketController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label inStockLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField countField;

    User user;
    Supplier supplier;

    @FXML
    void initialize() {}

    public void action() {
        submitBtn.setOnAction(event -> {
            submit();
        });

        cancelBtn.setOnAction(event -> {
            try {
                openWindow("/views/findSup.fxml", "Система оценки поставщиков");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void submit() {
        boolean isCount = false;
        if(countField.getText().matches("^\\d{1,}$") && Integer.parseInt(countField.getText()) > 0 && Integer.parseInt(countField.getText()) < 11 ) {
            isCount = true;
        }
        if(!isCount) {
            countField.setText("");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Введена неверная оценка!");
            alert.showAndWait();
        }else {
            Basket basket = new Basket();
            basket.setIdAccount(user.getIdUser());
            basket.setIdSupplier(supplier.getId());
            basket.setVision(true);
            basket.setCount(Integer.parseInt(countField.getText()));
            Client.getInstance().sendMessage("basketAdd");
            Client.getInstance().sendObject(basket);
            String answer = null;
            try {
                answer = Client.getInstance().readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Оповещение");
                alert.setHeaderText("Поставщик добавлен!");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    try {
                        openWindow("/views/findSup.fxml", "Система оценки поставщиков");
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
        if (title.equals("Система оценки поставщиков")) {
            FindSupController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Supplier supplier) {
        this.supplier = supplier;
        categoryLabel.setText(categoryLabel.getText() + " " + supplier.getTitleCategory());
        titleLabel.setText(titleLabel.getText() + " " + supplier.getTitle());
        descriptionLabel.setText(descriptionLabel.getText() + " " + supplier.getDescription());
    }
}
