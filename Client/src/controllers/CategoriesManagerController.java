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

public class CategoriesManagerController implements IController{

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
    private TableView<Category> table;

    @FXML
    private TableColumn<Category, Integer> idCol;

    @FXML
    private TableColumn<Category, String> titleCol;

    ArrayList<Category> categories;
    Category category;
    private final ObservableList<Category> observableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {}
    public void action(){
        fillTable();

        addBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/categoriesAdd.fxml", "Добавление");
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
        Client.getInstance().sendMessage("categoryAll");
        categories = (ArrayList<Category>) Client.getInstance().readObject();
        observableList.setAll(categories);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Category, String>("title"));
    }

    public void delete() throws IOException {
        String answer;
        if(table.getSelectionModel().getSelectedItem() != null){
            int delID = table.getSelectionModel().getSelectedItem().getId();
            Client.getInstance().sendMessage("categoryDelete");
            Client.getInstance().sendMessage(Integer.toString(delID));
            answer = Client.getInstance().readMessage();
            if(answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Уведомление");
                alert.setContentText("Выбранная категория успешно удалена!");
                alert.showAndWait();
                Category selectedItem = table.getSelectionModel().getSelectedItem();
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
            category = table.getSelectionModel().getSelectedItem();
            openWindow("/views/categoriesEdit.fxml", "Редактирование");
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
        if (title.equals("Редактирование")) {
            CategoriesEditController controller = loader.getController();
            controller.fillData(category);
            controller.action();
        } else if (title.equals("Администрирование")) {
            AdminMenuController controller = loader.getController();
            controller.action();
        } else if (title.equals("Добавление")) {
            CategoriesAddController controller = loader.getController();
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
