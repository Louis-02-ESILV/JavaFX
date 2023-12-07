import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {//Produit Data Access Object par nomenclature
    private static final String URL = "jdbc:mysql://localhost:3306/store";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "root";
    public void supprimerTousLesProduits() {
        try (Connection connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE)) {
            String requete = "DELETE FROM produits";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(requete)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Produit mapResultSetToProduit(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String nom = resultSet.getString("nom");
        double prix = resultSet.getDouble("prix");
        int stock = resultSet.getInt("stock");
        int taille = resultSet.getInt("taille");
        int pointure = resultSet.getInt("pointure");

        Produit produit;

        switch (type) {
            case "Vetement":
                produit = new Vetement(nom, prix, stock, taille);
                break;
            case "Chaussure":
                produit = new Chaussure(nom, prix, stock, pointure);
                break;
            // Ajoutez d'autres cas pour d'autres types si nécessaire
            default:
                produit = new Produit(nom, prix, stock);
                break;
        }

        produit.setId(id);
        return produit;
    }
    public List<Produit> obtenirTousLesProduits() {
        List<Produit> produits = new ArrayList<>();

        try (Connection connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE)) {
            String requete = "SELECT * FROM produits";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(requete)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Produit produit = mapResultSetToProduit(resultSet);
                        produits.add(produit);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public void ajouterProduit(Produit produit) {
        try (Connection connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE)) {
            String requete = "INSERT INTO produits (id, type, nom, prix, stock, taille, pointure) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(requete)) {
                preparedStatement.setInt(1, produit.getId());
                preparedStatement.setString(2, produit.getClass().getSimpleName()); // Automatically set the type based on class name
                preparedStatement.setString(3, produit.getNom());
                preparedStatement.setDouble(4, produit.getPrix());
                preparedStatement.setInt(5, produit.getNbex());

                if (produit instanceof Vetement) {
                    Vetement vetement = (Vetement) produit;
                    preparedStatement.setInt(6, vetement.getTaille());
                    preparedStatement.setNull(7, java.sql.Types.INTEGER);
                } else if (produit instanceof Chaussure) {
                    Chaussure chaussure = (Chaussure) produit;
                    preparedStatement.setNull(6, java.sql.Types.INTEGER);
                    preparedStatement.setInt(7, chaussure.getPointure());
                } else {
                    preparedStatement.setNull(6, java.sql.Types.INTEGER);
                    preparedStatement.setNull(7, java.sql.Types.INTEGER);
                }

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void mettreAJourProduit(Produit produit) {
        try (Connection connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE)) {
            String requete = "UPDATE produits SET nom=?, prix=?, stock=?, taille=?, pointure=? WHERE id=?";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(requete)) {
                preparedStatement.setString(1, produit.getNom());
                preparedStatement.setDouble(2, produit.getPrix());
                preparedStatement.setInt(3, produit.getNbex());

                if (produit instanceof Vetement) {
                    preparedStatement.setInt(4, ((Vetement) produit).getTaille());
                    preparedStatement.setNull(5, java.sql.Types.INTEGER);
                } else if (produit instanceof Chaussure) {
                    preparedStatement.setNull(4, java.sql.Types.INTEGER);
                    preparedStatement.setInt(5, ((Chaussure) produit).getPointure());
                } else {
                    preparedStatement.setNull(4, java.sql.Types.INTEGER);
                    preparedStatement.setNull(5, java.sql.Types.INTEGER);
                }

                preparedStatement.setInt(6, produit.getId());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
