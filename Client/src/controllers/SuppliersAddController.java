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
import models.Supplier;

public class SuppliersAddController implements IController{

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
    private Button categoryBtn;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

      Supplier supplier = new Supplier();

    public void setProduct(Supplier supplier) {
        this.supplier = supplier;
        descriptionField.setText(supplier.getDescription());
        titleField.setText(supplier.getTitle());
        categoryLabel.setText(supplier.getTitleCategory());
    }

    @FXML
    void initialize() {}

    public void action(){

        categoryBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/categoriesChoose.fxml", "Выбор категории");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        submitBtn.setOnAction(actionEvent -> {
            try {
                submit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        cancelBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/suppliersManager.fxml", "Управление поставщиками");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void openWindow(String path, String titl) throws IOException {
        if (titl.equals("Выбор категории")) {
            supplier.setDescription(descriptionField.getText());
            supplier.setTitle(titleField.getText());
            supplier.setTitleCategory(categoryLabel.getText());
        }
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();

        if (titl.equals("Управление поставщиками")) {
            SuppliersManagerController controller = loader.getController();
            controller.action();
        } else if (titl.equals("Выбор категории")) {
            CategoriesChooseController controller = loader.getController();
            controller.setProduct(supplier, "/views/suppliersAdd.fxml", "Добавление");
            controller.action();
        }
        stage.setTitle(titl);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void submit() throws IOException {
        boolean  isTitle = false, isCategory = false;


        if(!titleField.getText().isEmpty()) {
            isTitle = true;
        }

        if(supplier.getTitleCategory() != null) {
            isCategory = true;
        }

        if(isTitle && isCategory){
            supplier.setDescription(descriptionField.getText());
            supplier.setTitle(titleField.getText());
            supplier.setTitleCategory(categoryLabel.getText());

            Client.getInstance().sendMessage("productAdd");
            Client.getInstance().sendObject(supplier);
            String answer = Client.getInstance().readMessage();
            if (answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Оповещение");
                alert.setHeaderText("Поставщик добавлен!");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    openWindow("/views/suppliersManager.fxml", "Управление поставщиками");
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Данные введены некорректно!");
            alert.showAndWait();

            if(!isCategory) {
                categoryLabel.setText("Выберите категорию!!!");
            }
        }
    }
}
