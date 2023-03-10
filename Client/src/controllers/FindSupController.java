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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Supplier;
import models.User;

public class FindSupController implements  IController{

    @FXML
    private Button basketBtn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TableView<Supplier> table;

    @FXML
    private TableColumn<Supplier, Integer> idCol;

    @FXML
    private TableColumn<Supplier, String> titleCol;

    @FXML
    private TableColumn<Supplier, String> categoryCol;

    @FXML
    private TableColumn<Supplier, String> descriptionCol;

    @FXML
    private Button allProductsBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTitle;

    @FXML
    private Button allCategoriesBtn;

    @FXML
    private Button categoryBtn;

    User user;

    public void setUser(User user) {
        this.user = user;
    }

    ArrayList<Supplier> suppliers;
    Supplier supplier;

    private final ObservableList<Supplier> observableList = FXCollections.observableArrayList();


    @FXML
    void initialize() {}

    public void setSearchBtn(){
        if(!searchTitle.getText().isEmpty()){
            Client.getInstance().sendMessage("productTitle");
            Client.getInstance().sendMessage(searchTitle.getText());
            suppliers = (ArrayList<Supplier>) Client.getInstance().readObject();
            observableList.setAll(suppliers);
            table.setItems(observableList);
            idCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
            titleCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("title"));
            categoryCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("titleCategory"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("description"));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Поле поиска пустое!");
            alert.showAndWait();
        }
    }

    public void fillTable() {
        Client.getInstance().sendMessage("productAll");
        suppliers = (ArrayList<Supplier>) Client.getInstance().readObject();

        observableList.setAll(suppliers);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("titleCategory"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("description"));
    }

    public void order() throws IOException {
        if (table.getSelectionModel().getSelectedItem() != null) {
            supplier = table.getSelectionModel().getSelectedItem();
            openWindow("/views/addInBasket.fxml", "Выбрать поставщиков");
        } else {
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
        if (title.equals("Личный кабинет")) {
            UserMenuController controller = loader.getController();
            controller.setUser(user);
            controller.action();
        } else if (title.equals("Выбрать поставщиков")) {
            AddInBasketController controller = loader.getController();
            controller.setUser(user);
            controller.setProduct(supplier);
            controller.action();
        } else if (title.equals("Выбранные поставщики")) {
            BasketController controller = loader.getController();
            controller.setUser(user);
            controller.setPath("/views/findSup.fxml", "Система оценки поставщиков");
            controller.action();
        } else if (title.equals("Выбор категории")) {
            CategoriesChooseController controller = loader.getController();
            controller.setUser(user, "/views/findSup.fxml", "Система оценки поставщиков");
            controller.action();
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void action() {
        fillTable();

        categoryBtn.setOnAction(event -> {
            try {
                openWindow("/views/categoriesChoose.fxml", "Выбор категории");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        searchBtn.setOnAction(actionEvent -> {
            setSearchBtn();
        });

        allProductsBtn.setOnAction(actionEvent -> {
            fillTable();
        });

        allCategoriesBtn.setOnAction(actionEvent -> {
            fillTable();
        });

        cancelBtn.setOnAction(actionEvent -> {
            try {
                openWindow("/views/userMenu.fxml", "Личный кабинет");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        submitBtn.setOnAction(actionEvent -> {
            try {
                order();
                fillTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        basketBtn.setOnAction(event -> {
            try {
                openWindow("/views/basket.fxml", "Выбранные поставщики");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void categoryChoose(String title){
        Client.getInstance().sendMessage("productCategory");
        Client.getInstance().sendMessage(title);
        suppliers = (ArrayList<Supplier>) Client.getInstance().readObject();
        observableList.setAll(suppliers);
        table.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("titleCategory"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("description"));
    }

}

