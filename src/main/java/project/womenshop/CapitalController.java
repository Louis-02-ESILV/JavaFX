package project.womenshop;

import Database.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CapitalController implements Initializable {

    @FXML
    private MenuItem Finance;

    @FXML
    private MenuBar Menu;

    @FXML
    private MenuItem Stock;

    @FXML
    private Label ToutLabel;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private BarChart<String, Number> typeChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private CategoryAxis xAxist;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private NumberAxis yAxist;
    private static ProduitDAO produitDAO;
    private List<Produit> produits;
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private double benefice;
    private double depensetotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        produitDAO = new ProduitDAO();
        produits = produitDAO.obtenirTousLesProduits();
        Update_All();
        Random_Sell();
    }
    public void Update_All()
    {
        Update_Label();
        Update_barChart();
    }
    public void Update_Label()
    {
        ToutLabel.setText("Chiffre d'affaire : "+Math.round(Produit.getRecetteTot())+"€    / Bénéfice : "+Math.round(Produit.getBenefice())+"€");
    }
    public void Update_barChart(){
        barChart.getData().clear();
        Map<Class<? extends Produit>, List<Produit>> groupedProducts = produitDAO.obtenirTousLesProduits().stream()
                .collect(Collectors.groupingBy(Produit::getClass));

        for (Map.Entry<Class<? extends Produit>, List<Produit>> entry : groupedProducts.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Class<? extends Produit> productType = entry.getKey();

            String color;
            if (productType.equals(Vetement.class)) {
                color = "red";
            } else if (productType.equals(Accessoire.class)) {
                color = "blue";
            } else if (productType.equals(Chaussure.class)) {
                color = "green";
            } else {
                color = "black"; // Default color if no match is found
            }

            for (Produit produit : entry.getValue()) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(produit.getNom(), produit.getRecetteProd());
                series.getData().add(data);

                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: " + color + ";");
                }
            }

            barChart.getData().add(series);
            series.setName(productType.getSimpleName()); // Set series name for legend
        }

        xAxis.setLabel("Produits");
        yAxis.setLabel("Quantitées");

        // Add legend to the barChart
        barChart.setLegendVisible(true);
        barChart.setStyle("-fx-bar-fill: black; -fx-font-size: 14px;");
    }

    @FXML
    void OnClick_Stock(ActionEvent event) {
        try {
            executorService.shutdown();
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
    public void Random_Sell()
    {
        Random random = new Random();
        executorService.scheduleAtFixedRate(() -> {
            sellProduct(produits.get(random.nextInt(produits.size())));
            Platform.runLater(() -> {
                Update_barChart();
            });
        }, 0, 5, TimeUnit.SECONDS);
        // To stop the executor service after a certain duration (e.g., 60 seconds)
        executorService.schedule(() -> executorService.shutdown(), 60, TimeUnit.SECONDS);
    }

    public static void sellProduct(Produit product) {
        Random random = new Random();
        var nbvente = random.nextInt(product.getNbex()/50);
        System.out.println("Sold " + product.getNom() + " for €" + product.getPrix()*nbvente +" recette :"+Produit.getRecetteTot());
        product.Vendre(nbvente);
        produitDAO.mettreAJourProduit(product);
    }
}
