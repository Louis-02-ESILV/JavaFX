import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.JDBCType;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produits=produitDAO.obtenirTousLesProduits();
        System.out.println(produits.get(0).toString());
        produits.get(0).Vendre(6);
        produitDAO.mettreAJourProduit(produits.get(0));
        System.out.println(produits.get(0).toString());
        /*Initiliser des produits basiques :
        produitDAO.supprimerTousLesProduits();
        Vetement jean = new Vetement( "Jean", 29.99, 50, 38);
        produitDAO.ajouterProduit(jean);
        Chaussure sandales = new Chaussure( "Sandales", 29.99, 50, 38);
        produitDAO.ajouterProduit(sandales);
        Accessoire sac = new Accessoire("Sac",50,2);
        produitDAO.ajouterProduit(sac);
         */
    }
}