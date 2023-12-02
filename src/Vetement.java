public class Vetement extends Produit{
    private int taille;

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public Vetement(String nom, double prix, int nbex, int taille){
        super(nom,prix,nbex);//Gets the attributes from the extended class
        if(taille>=34&& taille <=54){this.taille=taille;}
        else{throw new IllegalArgumentException("Taille erronée");}
    }

    @Override
    public String toString() {
        return super.toString()+", taille ="+this.taille;
    }
}
