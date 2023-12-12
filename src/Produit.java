import java.util.concurrent.atomic.AtomicInteger;

//Classe prosuit qui se caractérise comme base de l'hérédité
//Attributs Numéro auto incrementé, nom, prix et nombre d'exemplaires
public class Produit {
    private static AtomicInteger count = new AtomicInteger(0);
    private int id;
    private int prix;
    private String nom;
    private int nbex;
    private static int recette=0;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Produit(String nom, int prix, int nbex){
        this.nom=nom;
        if(prix >0)
        {this.prix=prix;}
        else{throw new IllegalArgumentException("Prix négatif !!");}
        if(nbex>=0){
        this.nbex=nbex;}
        else{throw new IllegalArgumentException("nbex négatif !!!!");}
        this.id=count.incrementAndGet();
    }
    public int getNbex() {
        return nbex;
    }
    public void setNbex(int nbex) {
        this.nbex = nbex;
    }
    public int getPrix() {
        return prix;
    }
    public void setPrix(int prix) {
        this.prix = prix;
    }
    public int getId() {
        return id;
    }
    public void Vendre(int qtt){
        if(this.nbex>=qtt){
            this.nbex-=qtt;
            System.out.println("Stock modifié avec succès");
        }else{
            throw new IllegalArgumentException("Produit Indisponible");
        }
    }
    public void Achat(int qtt){
        if (qtt>0){this.nbex+=qtt;}
        else{
            System.out.println("Achat négatif !!!");
        }
    }
    public void setId(int id) {
        this.id = id;
    }
    public String toString(){
        return "ID:"+this.id+" ,prix : "+this.prix+" ,nb exemplaires : "+this.nbex;
    }
}
