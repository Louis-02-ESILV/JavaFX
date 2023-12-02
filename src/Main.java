import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.JDBCType;
public class Main {
    public static void main(String[] args) {
        ProduitDAO produitDAO = new ProduitDAO();
        produitDAO.supprimerTousLesProduits();
        Vetement jean = new Vetement( "Jean", 29.99, 50, 38);
        produitDAO.ajouterProduit(jean);
        Chaussure sandales = new Chaussure( "Sandales", 29.99, 50, 38);
        produitDAO.ajouterProduit(sandales);
        Accessoire sac = new Accessoire("Sac",50,2);
        produitDAO.ajouterProduit(sac);
    }
}