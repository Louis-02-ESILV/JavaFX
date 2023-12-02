import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProduitDAO {
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

}
