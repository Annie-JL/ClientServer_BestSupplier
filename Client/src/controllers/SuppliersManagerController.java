package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Supplier;

public class SuppliersManagerController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button canselBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button addBtn;

    @FXML
    private TableView<Supplier> table;

    @FXML
    private TableColumn<Supplier, Integer> idCol;

    @FXML
    private TableColumn<Supplier, String> categoryCol;

    @FXML
    private TableColumn<Supplier, String> titleCol;

    @FXML
    private TableColumn<Supplier, String> descriptionCol;


    ArrayList<Supplier> suppliers;
    Supplier supplier;

    private final ObservableList<Supplier> observableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {}
    public void action(){
        fillTable();

        addBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/suppliersAdd.fxml", "Добавление");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        editBtn.setOnAction(actionEvent -> {
            try {
                edit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        canselBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/adminMenu.fxml", "Администрирование");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteBtn.setOnAction(actionEvent -> {
            try {
                delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    public void fillTable() {
        Client.getInstance().sendMessage("productAll");
        suppliers = (ArrayList<Supplier>) Client.getInstance().readObject();
        observableList.setAll(suppliers);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("titleCategory"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("description"));

    }

    public void delete() throws IOException {
        String answer;
        if(table.getSelectionModel().getSelectedItem() != null){
            int delID = table.getSelectionModel().getSelectedItem().getId();
            Client.getInstance().sendMessage("productDelete");
            Client.getInstance().sendMessage(Integer.toString(delID));
            answer = Client.getInstance().readMessage();
            if(answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Уведомление");
                alert.setContentText("Выбранный поставщик успешно удален!");
                alert.showAndWait();
                Supplier selectedItem = table.getSelectionModel().getSelectedItem();
                table.getItems().remove(selectedItem);
            }
            else if(answer.equals("false")){
                System.out.println("ГГ");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Вы не выбрали запись!");
            alert.showAndWait();
        }
    }

    public void edit() throws IOException {
        if(table.getSelectionModel().getSelectedItem() != null){
            supplier = table.getSelectionModel().getSelectedItem();
            openWindow("/views/suppliersEdit.fxml", "Редактирование");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Вы не выбрали запись!");
            alert.showAndWait();
        }
    }

    @Override
    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) canselBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Администрирование")) {
            AdminMenuController controller = loader.getController();
            controller.action();
        } else if (title.equals("Добавление")) {
            SuppliersAddController controller = loader.getController();
            controller.action();
        }
        else if (title.equals("Редактирование")) {
            SuppliersEditController controller = loader.getController();
            controller.setProduct(supplier);
            controller.action();
        }
        else if (title.equals("Редактирование")) {
            SuppliersEditController controller = loader.getController();
            controller.setProduct(supplier);
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
