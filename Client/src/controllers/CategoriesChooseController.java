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
import models.Category;
import models.Supplier;
import models.User;

public class CategoriesChooseController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TableView<Category> table;

    @FXML
    private TableColumn<Category, Integer> idCol;

    @FXML
    private TableColumn<Category, String> titleCol;

    String path;
    String nazva;
    Boolean cancel = false;
    User user;
    Supplier supplier = null;

    public void setUser(User user, String path, String title){
        this.user = user;
        this.path = path;
        nazva = title;
    }

    public void setProduct(Supplier supplier, String path, String title) {
        this.supplier = supplier;
        this.path = path;
        nazva = title;
    }

    ArrayList<Category> categories;
    Category category;
    private final ObservableList<Category> observableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {}

    @FXML
    public void action(){

        fillTable();

        cancelBtn.setOnAction(actionEvent -> {
            cancel = true;
            try {
                openWindow(path, nazva);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        submitBtn.setOnAction(actionEvent -> {
            try {
                choose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void fillTable() {
        Client.getInstance().sendMessage("categoryAll");
        categories = (ArrayList<Category>) Client.getInstance().readObject();
        observableList.setAll(categories);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Category, String>("title"));
    }

    public void choose() throws IOException {
//        String answer;
        if(table.getSelectionModel().getSelectedItem() != null){
            category = table.getSelectionModel().getSelectedItem();
            if(supplier !=null){
                supplier.setIdCategory(category.getId());
                supplier.setTitleCategory(category.getTitle());
            }

            openWindow(path, nazva);
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
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Добавление")) {
                SuppliersAddController controller = loader.getController();
                controller.setProduct(supplier);
                controller.action();
        } else if(title.equals("Редактирование")){
                SuppliersEditController controller = loader.getController();
                controller.setProduct(supplier);
                controller.action();
        } else if(title.equals("Магазин")){
            FindSupController controller = loader.getController();
            controller.setUser(user);
            controller.action();
            if (cancel==false) {
                controller.categoryChoose(category.getTitle());
            }
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
