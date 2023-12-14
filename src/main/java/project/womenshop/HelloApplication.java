package project.womenshop;

import Database.Produit;
import Database.ProduitDAO;
import Database.Vetement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("WomenShop!");
        Image icon = new Image("C:\\Users\\annel\\OneDrive - De Vinci\\cours\\S07\\Oriented Object Developement\\JavaFX\\src\\main\\resources\\logo/icon.png"); // Provide the path to your icon image
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}