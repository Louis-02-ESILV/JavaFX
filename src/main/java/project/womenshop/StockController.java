package project.womenshop;

import Database.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class StockController implements Initializable {

    @FXML
    private MenuItem Finance;

    @FXML
    private TextField IDArea;

    @FXML
    private Text Label_Produit;

    @FXML
    private MenuBar Menu;

    @FXML
    private MenuItem MenuAchat;

    @FXML
    private MenuItem MenuStock;

    @FXML
    private TextField NameArea;

    @FXML
    private TextField PriceArea;
    @FXML
    private TextField PointureArea;
    @FXML
    private TextField SizeArea;
    @FXML
    private Label ValidationLabel;

    @FXML
    private ChoiceBox<String> ProductType;

    @FXML
    private MenuItem Stock;

    @FXML
    private TextField StockArea;
    @FXML
    private VBox SizeBox;
    @FXML
    private VBox PointureBox;

    @FXML
    private TableColumn<Produit, Integer> idColumn;

    @FXML
    private TableColumn<Produit, String> nomColumn;

    @FXML
    private TableColumn<Produit, Double> prixColumn;

    @FXML
    private TableColumn<Produit, Integer> stockColumn;

    @FXML
    private TableColumn<Produit, Integer> stockPointure;

    @FXML
    private TableColumn<Produit, Integer> stockTaille;
    @FXML
    private TableColumn<Produit, String> typeProduitColumn;
    @FXML
    private TableColumn<Produit, Void> modifierStockColumn;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private VBox legendBox;



    @FXML
    private TableView<Produit> tableauStock;
    private static ProduitDAO produitDAO;
    private List<Produit> produits;
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        produitDAO = new ProduitDAO();
        produits = produitDAO.obtenirTousLesProduits();
        updateTable(produits);
        modifierStockColumn.setCellFactory(param -> new TableCell<>() {
            private final javafx.scene.control.Button btnModifierStock = new javafx.scene.control.Button("Modifier");
            private final TextField textFieldStock = new TextField();

            {
                btnModifierStock.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    int newStock = Integer.parseInt(textFieldStock.getText());
                    System.out.println("Nouveau stock pour " + produit.getNom() + " : " + newStock);
                    if (newStock > produit.getNbex()) {
                        produit.Achat(newStock - produit.getNbex());
                    } else {
                        produit.Supprimer(produit.getNbex() - newStock);
                    }
                    produitDAO.mettreAJourProduit(produit);
                    updateTable(produitDAO.obtenirTousLesProduits());
                    tableauStock.refresh();
                    Update_Chart();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Produit produit = getTableView().getItems().get(getIndex());
                    textFieldStock.setText(String.valueOf(produit.getNbex()));
                    setGraphic(new VBox(5, textFieldStock, btnModifierStock));
                }
            }
        });
        Random_Sell();
    }
    public void Update_Chart() {
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
                XYChart.Data<String, Number> data = new XYChart.Data<>(produit.getNom(), produit.getNbex());
                series.getData().add(data);

                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: " + color + ";");
                }
            }

            barChart.getData().add(series);
            series.setName(productType.getSimpleName()); // Set series name for legend
        }

        // Configure axis labels
        xAxis.setLabel("Clothing");
        yAxis.setLabel("Quantity");

        // Add legend to the BarChart
        barChart.setLegendVisible(true);
        barChart.setStyle("-fx-bar-fill: black; -fx-font-size: 14px;");
    }



    public void updateTable(List<Produit> produits) {
        ObservableList<Produit> data = FXCollections.observableArrayList(produits);
        typeProduitColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Vetement) {
                return new SimpleStringProperty("Vêtement");
            } else if (cellData.getValue() instanceof Chaussure) {
                return new SimpleStringProperty("Chaussure");
            } else if (cellData.getValue() instanceof Accessoire) {
                return new SimpleStringProperty("Accessoire");
            } else {
                return new SimpleStringProperty("Inconnu");
            }
        });
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Produit) cellData.getValue()).getId()).asObject());
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Produit) cellData.getValue()).getNom()));
        prixColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(((Produit) cellData.getValue()).getPrix()).asObject());
        stockColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Produit) cellData.getValue()).getNbex()).asObject());

        stockPointure.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Chaussure) {
                return new SimpleIntegerProperty(((Chaussure) cellData.getValue()).getPointure()).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject(); // Si ce n'est pas une chaussure, affiche 0
            }
        });

        stockTaille.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Vetement) {
                return new SimpleIntegerProperty(((Vetement) cellData.getValue()).getTaille()).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject(); // Si ce n'est pas un vêtement, affiche 0
            }
        });
        tableauStock.setItems(data);
        SetIdNewObject();
        Update_Chart();
    }
    void SetIdNewObject()
    {
        int availableID = 1; // Start with the first ID

        // Loop through the products list to find the first available ID
        for (Produit produit : produitDAO.obtenirTousLesProduits()) {
            if (produit.getId() == availableID) {
                availableID++; // Increment to check the next ID
            } else {
                break; // Break the loop if there's a gap in IDs
            }
        }
        IDArea.setText(String.valueOf(availableID));
    }

    @FXML
    void OnClickFinance()
    {
        try {
            switchToScene2();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void switchToScene2() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Capital.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) Stock.getParentPopup().getOwnerWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void Clear_Text()
    {
        NameArea.clear();
        PriceArea.clear();
        SizeArea.clear();
        StockArea.clear();
        PointureArea.clear();
    }
    @FXML
    private void TypeSelected()
    {
        String value =ProductType.getValue();
        if (Objects.equals(value, "Vêtement"))
        {
            SizeBox.setDisable(false);
            PointureBox.setDisable(true);
            PointureBox.setOpacity(0.2);
            Clear_Text();

        } else if (Objects.equals(value,"Chaussure")) {
            PointureBox.setDisable(false);
            SizeBox.setDisable(true);
            SizeBox.setOpacity(0.2);
            Clear_Text();
        }
        else
        {
            PointureBox.setDisable(true);
            PointureBox.setOpacity(0.2);
            SizeBox.setDisable(true);
            SizeBox.setOpacity(0.2);
            Clear_Text();
        }
    }
    @FXML
    public void OnClick_Validation() {
        try {
            String type = ProductType.getSelectionModel().getSelectedItem();
            String nom = NameArea.getText();
            double prix = Double.parseDouble(PriceArea.getText());
            int stock = Integer.parseInt(StockArea.getText());
            int id = Integer.parseInt(IDArea.getText());

            if (nom.isEmpty() || prix <= 0 || stock < 0) {
                throw new IllegalArgumentException("Veuillez remplir correctement tous les champs.");
            }

            switch (type) {
                case "Vêtement" -> {
                    int special = Integer.parseInt(SizeArea.getText());
                    if (special < 34 || special > 54) {
                        throw new IllegalArgumentException("La taille doit être comprise entre 34 et 54.");
                    }
                    Vetement newproduit = new Vetement(id,nom, prix, stock, special);
                    ValidationLabel.setText("Vêtement créé : " + newproduit.toString());
                    produitDAO.ajouterProduit(newproduit);

                }
                case "Accessoire" -> {
                    Accessoire newproduit = new Accessoire(id,nom, prix, stock);
                    ValidationLabel.setText("Accessoire créé : " + newproduit.toString());
                    produitDAO.ajouterProduit(newproduit);

                }
                case "Chaussure" -> {
                    int pointure = Integer.parseInt(PointureArea.getText());
                    if (pointure < 34 || pointure > 54) {
                        throw new IllegalArgumentException("La pointure doit être comprise entre 32 et 50.");
                    }
                    Chaussure newproduit = new Chaussure(id,nom, prix, stock, pointure);
                    ValidationLabel.setText("Produit créé : " + newproduit.toString());
                    produitDAO.ajouterProduit(newproduit);
                }
                default-> {
                    ValidationLabel.setText("Type de produit non reconnu");
                }
            }
            updateTable(produitDAO.obtenirTousLesProduits());
        } catch (NumberFormatException e) {
            ValidationLabel.setText("Veuillez saisir des valeurs numériques valides.");
        } catch (IllegalArgumentException e) {
            ValidationLabel.setText("Erreur : " + e.getMessage());
        } catch (Exception e) {
            ValidationLabel.setText("Une erreur est survenue : " + e.getMessage());
        }
        Clear_Text();
    }
    public void Random_Sell()
    {
        Random random = new Random();
        executorService.scheduleAtFixedRate(() -> {
            sellProduct(produits.get(random.nextInt(produits.size())));
            Platform.runLater(() -> {
                updateTable(produitDAO.obtenirTousLesProduits());
            });
        }, 0, 5, TimeUnit.SECONDS);
        // To stop the executor service after a certain duration (e.g., 60 seconds)
        executorService.schedule(() -> executorService.shutdown(), 60, TimeUnit.SECONDS);
    }

    public static void sellProduct(Produit product) {
        Random random = new Random();
        int nbvente = random.nextInt(product.getNbex()/50);
        System.out.println("produit " + product.getNom() + " pour un total en €" + product.getPrix());
        product.Vendre(nbvente);
        produitDAO.mettreAJourProduit(product);
    }
}

