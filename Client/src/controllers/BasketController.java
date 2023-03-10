package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Basket;
import models.User;

public class BasketController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button editBtn;

    @FXML
    private TableView<Basket> table;

    @FXML
    private TableColumn<Basket, Integer> idCol;

    @FXML
    private TableColumn<Basket, String> titleCol;


    @FXML
    private TableColumn<Basket, Integer> orderCol;


    @FXML
    private Label Score;

    @FXML
    private Button submitBtn;

    String path;
    String title;
    ArrayList<Basket> baskets;
    Basket basket;
    Basket maxB;
    String maxxx;
    User user;

    private final ObservableList<Basket> observableList = FXCollections.observableArrayList();


    @FXML
    void initialize() {}

    public void action() {
        fillTable();

        submitBtn.setOnAction(event -> {
            submit();
        });

        deleteBtn.setOnAction(event -> {
            try {
                delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        editBtn.setOnAction(event -> {
            edit();
        });

        cancelBtn.setOnAction(event -> {
            try {
                openWindow(path, title);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void openWindow(String path, String titl) throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (titl.equals("Система оценки поставщиков")) {
            FindSupController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        } else if (titl.equals("Личный кабинет")) {
            UserMenuController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        } else if (titl.equals("Редактирование записи")) {
            EditInBasketController controller = loader.getController();
            controller.setUser(user);
            controller.setBasket(basket);
            controller.setPath(this.path, this.title);
            controller.action();
        }
        stage.setTitle(titl);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void fillTable() {
        Client.getInstance().sendMessage("basketAll");
        Client.getInstance().sendMessage(String.valueOf(user.getIdUser()));
        baskets = (ArrayList<Basket>) Client.getInstance().readObject();
        float max = 0;
        for (Basket ba : baskets) {
            if (max < ba.getCount()) {
                max = ba.getCount();
                maxB = ba;
            }
        }
        try {
            maxxx = maxB.getSupplierTitle();
            Score.setText(String.valueOf(max));
        } catch (Exception e) {

        }

        observableList.setAll(baskets);


        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Basket, Integer>("idSupplier"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Basket, String>("supplierTitle"));
        orderCol.setCellValueFactory(new PropertyValueFactory<Basket, Integer>("count"));
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPath(String path, String title) {
        this.path = path;
        this.title = title;
    }

    private void edit(){
        if(table.getSelectionModel().getSelectedItem() != null){
            basket = table.getSelectionModel().getSelectedItem();
            try {
                openWindow("/views/editInBasket.fxml", "Редактирование записи");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Вы не выбрали запись!");
            alert.showAndWait();
        }
    }

    private void delete() throws IOException {
        String answer;
        if(table.getSelectionModel().getSelectedItem() != null){
            int delID = table.getSelectionModel().getSelectedItem().getId();
            Client.getInstance().sendMessage("basketDelete");
            Client.getInstance().sendMessage(Integer.toString(delID));
            answer = Client.getInstance().readMessage();
            if(answer.equals("true")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Уведомление");
                alert.setContentText("Выбранная запись успешно удалена!");
                alert.showAndWait();
                Basket selectedItem = table.getSelectionModel().getSelectedItem();
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

    private void submit() {
        Client.getInstance().sendMessage("basketPay");
        Client.getInstance().sendObject(baskets);

        String answer = null;
        try {
            answer = Client.getInstance().readMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (answer.equals("true")) {
            fillTable();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Лучший поставщик из выбранных");
            alert.setHeaderText(maxxx);
            Optional<ButtonType> option = alert.showAndWait();
        }
    }
}
