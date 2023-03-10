package server;

import databaseManagement.*;
import models.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Handler implements Runnable {
    protected Socket clientSocket;
    ObjectInputStream sois;
    ObjectOutputStream soos;

    public Handler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            sois = new ObjectInputStream(clientSocket.getInputStream());
            soos = new ObjectOutputStream(clientSocket.getOutputStream());
            User user;
            Basket basket;
            Category category;
            Supplier supplier;
            String answer;
            int delId ;
            ArrayList<User> users;
            ArrayList<Basket> baskets = null;
            ArrayList<Order> orders = null;
            ArrayList<Category> categories;
            ArrayList<Supplier> supplierArray;
            while (true) {
                String choice = sois.readObject().toString();
                System.out.println(clientSocket.getPort() + " : " + choice);

                switch (choice) {
                    case "logIn":
                        user = (User)sois.readObject();
                        user = SQLUser.getInstance().logIn(user);
                        if(user.getRole().equals("undefined")){
                            soos.writeObject("undefined");
                        }
                        else if(user.getRole().equals("admin")){
                            soos.writeObject("admin");
                            soos.writeObject(user);
                        }
                        else if(user.getRole().equals("user")){
                            soos.writeObject("user");
                            soos.writeObject(user);
                        }
                        break;

                    case "registration":
                        user = (User)sois.readObject();
                        if(SQLUser.getInstance().registration(user).equals("true")){
                            soos.writeObject("true");
                        }
                        else{
                            soos.writeObject("false");
                        }
                        break;

                    case "allUsers":
                        users = SQLUser.getInstance().getUsers();
                        soos.writeObject(users);
                        break;

                    case "deleteUser":
                        delId = Integer.parseInt(sois.readObject().toString());
                        SQLUser.getInstance().delete(delId);
                        soos.writeObject("true");
                        break;

                    case "editUser":
                        user = (User)sois.readObject();
                        if (SQLUser.getInstance().editUser(user).equals("true")){
                            soos.writeObject("true");
                        }
                        else {
                            soos.writeObject("false");
                        }

                        break;

                    case "basketDelete":
                        delId = Integer.parseInt(sois.readObject().toString());
                        SQLBasket.getInstance().delete(delId);
                        soos.writeObject("true");
                        break;

                    case "basketPay":
                        ArrayList<Basket> basketsForPay = (ArrayList<Basket>)sois.readObject();
                        SQLBasket.getInstance().findMax(basketsForPay);
                        soos.writeObject("true");
                        break;

                    case "basketEdit":
                        basket = (Basket)sois.readObject();
                        SQLBasket.getInstance().edit(basket);
                        soos.writeObject("true");
                        break;

                    case "basketAdd":
                        basket = (Basket)sois.readObject();
                        SQLBasket.getInstance().add(basket);
                        soos.writeObject("true");
                        break;

                    case "basketAll":
                        String id = sois.readObject().toString();
                        baskets = SQLBasket.getInstance().get(id);
                        soos.writeObject(baskets);
                        break;


                    case "orderAll":
                        String idUser = sois.readObject().toString();
                        orders = SQLOrder.getInstance().get(idUser);
                        soos.writeObject(orders);
                        break;

                    case "productDelete":
                        delId = Integer.parseInt(sois.readObject().toString());
                        SQLSuppliers.getInstance().delete(delId);
                        soos.writeObject("true");
                        break;

                    case "productEdit":
                        supplier = (Supplier) sois.readObject();
                        SQLSuppliers.getInstance().edit(supplier);
                        soos.writeObject("true");
                        break;

                    case "productAdd":
                        supplier = (Supplier) sois.readObject();
                        SQLSuppliers.getInstance().add(supplier);
                        soos.writeObject("true");
                        break;

                    case "productAll":
                        supplierArray = SQLSuppliers.getInstance().get();
                        soos.writeObject(supplierArray);
                        break;

                    case "productTitle":
                        String title = sois.readObject().toString();
                        supplierArray = SQLSuppliers.getInstance().searchTitle(title);
                        soos.writeObject(supplierArray);
                        break;

                    case "productCategory":
                        String productCategory = sois.readObject().toString();
                        supplierArray = SQLSuppliers.getInstance().searchCategory(productCategory);
                        soos.writeObject(supplierArray);
                        break;

                    case "categoryDelete":
                        delId = Integer.parseInt(sois.readObject().toString());
                        SQLCategory.getInstance().delete(delId);
                        soos.writeObject("true");
                        break;

                    case "categoryEdit":
                        category = (Category) sois.readObject();
                        SQLCategory.getInstance().edit(category);
                        soos.writeObject("true");
                        break;

                    case "categoryAdd":
                        category = (Category) sois.readObject();
                        SQLCategory.getInstance().add(category);
                        soos.writeObject("true");
                        break;

                    case "categoryAll":
                        categories = SQLCategory.getInstance().get();
                        soos.writeObject(categories);
                        break;


                }
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
}
