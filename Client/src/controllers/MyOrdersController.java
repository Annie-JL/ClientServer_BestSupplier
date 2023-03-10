package controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import models.Basket;
import models.Category;
import models.Order;
import models.User;

public class MyOrdersController implements IController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button cancelBtn;

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Integer> idCol;

    @FXML
    private TableColumn<Order, Float> priceCol;

    @FXML
    private TableColumn<Order, Date> dateCol;

    @FXML
    private TableColumn<Order, String> BestS;

    @FXML
    private Button billBtn;

    User user;

    ArrayList<Order> orders;
    ArrayList<Basket> baskets;
    Order order;
    private final ObservableList<Order> observableList = FXCollections.observableArrayList();

    public void setUser(User user){
        this.user = user;
    }

    @FXML
    void initialize() { }

    public void action(){
        fillTable();

        cancelBtn.setOnAction(event -> {
            try {
                openWindow("/views/userMenu.fxml", "Личный кабинет");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private void bill() {
        if(table.getSelectionModel().getSelectedItem() != null){
            order = table.getSelectionModel().getSelectedItem();
            Client.getInstance().sendMessage("basketBill");
            Client.getInstance().sendMessage(String.valueOf(user.getIdUser()));
            Client.getInstance().sendMessage(String.valueOf(order.getId()));
            baskets = (ArrayList<Basket>) Client.getInstance().readObject();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Вы не выбрали запись!");
            alert.showAndWait();
        }
    }

    public void fillTable() {
        Client.getInstance().sendMessage("orderAll");
        Client.getInstance().sendMessage(String.valueOf(user.getIdUser()));
        orders = (ArrayList<Order>) Client.getInstance().readObject();
        observableList.setAll(orders);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Order, Float>("score"));
        BestS.setCellValueFactory(new PropertyValueFactory<Order, String>("BestS"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Order, Date>("date"));

    }

    @Override
    public void openWindow(String path, String title) throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        if (title.equals("Личный кабинет")) {
            UserMenuController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
