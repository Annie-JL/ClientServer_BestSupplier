package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import models.User;

public class UsersManagerController implements IController{

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
    private TableView<User> table;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private TableColumn<User, String> loginCol;

    @FXML
    private TableColumn<User, String> roleCol;

    @FXML
    private TableColumn<User, String> fioCol;

    @FXML
    private TableColumn<User, String> phoneCol;

    @FXML
    private TableColumn<User, String> genderCol;


    ArrayList<User> users;


    User userEdit;

    private final ObservableList<User> observableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {}

    public void action() {

        fillTable();

        addBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/userAdd.fxml", "Добавление");
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
        Client.getInstance().sendMessage("allUsers");
        users = (ArrayList<User>) Client.getInstance().readObject();
        observableList.setAll(users);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("idUser"));
        loginCol.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        roleCol.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        fioCol.setCellValueFactory(new PropertyValueFactory<User, String>("fio"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        genderCol.setCellValueFactory(new PropertyValueFactory<User, String>("gender"));
    }

    public void delete() throws IOException {
        String answer;
        if(table.getSelectionModel().getSelectedItem() != null){
            int delID = table.getSelectionModel().getSelectedItem().getIdUser();
            Client.getInstance().sendMessage("deleteUser");
            Client.getInstance().sendMessage(Integer.toString(delID));
            answer = Client.getInstance().readMessage();
            if(answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Уведомление");
                alert.setContentText("Выбранный пользователь успешно удален!");
                alert.showAndWait();
                User selectedItem = table.getSelectionModel().getSelectedItem();
                table.getItems().remove(selectedItem);
            }
            else if(answer.equals("false")){
                System.out.println("ГГ");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Вы не выбрали пользователя!");
            alert.showAndWait();
        }
    }

    public void edit() throws IOException {
        String answer;
        if(table.getSelectionModel().getSelectedItem() != null){
            userEdit = table.getSelectionModel().getSelectedItem();
            openWindow("/views/userEdit.fxml", "Редактирование");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Вы не выбрали пользователя!");
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
        if (title.equals("Добавление")) {
            UserAddController controller = loader.getController();
            controller.action();
        } else if (title.equals("Редактирование")) {
            UserEditController controller = loader.getController();
            controller.fillData(userEdit);
            controller.action();
        } else if (title.equals("Администрирование")) {
            AdminMenuController controller = loader.getController();
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
