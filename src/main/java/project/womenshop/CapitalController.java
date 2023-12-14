package project.womenshop;

import Database.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.*;

import java.io.IOException;

public class CapitalController implements Initializable {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private MenuItem Finance;

    @FXML
    private MenuBar Menu;

    @FXML
    private TextField SizeArea;

    @FXML
    private VBox SizeBox;

    @FXML
    private Text SizeText;

    @FXML
    private MenuItem Stock;
    private ProduitDAO produitDAO;
    private List<Produit> produits;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        produitDAO = new ProduitDAO();
        produits = produitDAO.obtenirTousLesProduits();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Product Quantity");

        Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
        Map<String, String> colorMap = Map.of(
                "Vetement", "red",
                "Chaussure", "blue",
                "Accessoire", "green"
        );

        for (String type : colorMap.keySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(type);
            seriesMap.put(type, series);
        }

        for (Produit produit : produits) {
            String productType = "";
            if (produit instanceof Vetement) {
                productType = "Vetement";
            } else if (produit instanceof Chaussure) {
                productType = "Chaussure";
            } else if (produit instanceof Accessoire) {
                productType = "Accessoire";
            }

            XYChart.Series<String, Number> series = seriesMap.get(productType);
            if (series != null) {
                series.getData().add(new XYChart.Data<>(produit.getNom(), produit.getNbex()));
            }
        }

        barChart.getData().addAll(seriesMap.values());

// Customize the color of each series
        for (String type : seriesMap.keySet()) {
            Node node = seriesMap.get(type).getNode();
            String color = colorMap.get(type);
            if (node != null && color != null) {
                node.setStyle("-fx-bar-fill: " + color + ";");
            }
        }
    }

    @FXML
    void OnClick_Stock(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Stock.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Stock.getParentPopup().getOwnerWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
