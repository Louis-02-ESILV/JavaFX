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
