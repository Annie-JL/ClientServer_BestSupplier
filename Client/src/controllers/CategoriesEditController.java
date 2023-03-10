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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Category;

public class CategoriesEditController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField title;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    Category category;

    public void fillData(Category ha){
        category = ha;
        title.setText(ha.getTitle());
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
                openWindow("/views/categoriesManager.fxml", "Управление категориями");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void submit() throws IOException {
//        boolean isLogin = false, isPassword = false, isPhoneNumber = false, isFio = false, isDate = false, isGender = false, isRole=false;
//
//        if(login.getText().matches("[a-zA-Z\\d]{4,20}")) {
//            isLogin = true;
//        }
//
//        if(password.getText().matches("[\\S]{4,20}")) {
//            isPassword = true;
//        }
//
//        if(fio.getText().matches("[а-яА-я\\s]{5,45}")) {
//            isFio = true;
//        }
//
//        if(phoneNumber.getText().matches("^((\\+375)[\\- ]?)?\\(?\\d{2}\\)?[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}(([\\- ]?\\d{1})?[\\- ]?\\d{1})?$")) {
//            isPhoneNumber = true;
//        }
//
//        if(dataOfBirth.getValue() != null) {
//            isDate = true;
//        }
//
//        if(gender.getValue() != null) {
//            isGender = true;
//        }
//
//        if(role.getValue() != null) {
//            isRole = true;
//        }
//
//        if(isLogin && isFio && isPassword && isPhoneNumber && isDate && isGender && isRole){
        if(true){
            Category ha = new Category();
            ha.setId(category.getId());
            ha.setTitle(title.getText());
            Client.getInstance().sendMessage("categoryEdit");
            Client.getInstance().sendObject(ha);
            String answer = Client.getInstance().readMessage();
            if (answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Оповещение");
                alert.setHeaderText("Категория успешно обновлена!");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    openWindow("/views/categoriesManager.fxml", "Управление категориями");
                }
            }
        }
//        else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Ошибка");
//            alert.setContentText("Данные введены некорректно!");
//            alert.showAndWait();
//            if(!isLogin) {
//                login.setText("");
//            }
//            if(!isFio) {
//                fio.setText("");
//            }
//            if(!isPhoneNumber){
//                phoneNumber.setText("");
//            }
//            if(!isPassword){
//                password.setText("");
//            }
//        }
    }

    @Override
    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Управление категориями")) {
            CategoriesManagerController controller = loader.getController();
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
