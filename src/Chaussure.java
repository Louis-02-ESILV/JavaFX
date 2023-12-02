public class Chaussure extends Produit{
    public int getPointure() {
        return pointure;
    }

    public void setPointure(int pointure) {
        this.pointure = pointure;
    }

    private int pointure;
    public Chaussure(String nom,double prix, int nbex, int pointure){
        super(nom,prix,nbex);
        if(pointure >= 36 && pointure<= 50){
            this.pointure=pointure;
        }
        else{
            throw new IllegalArgumentException("Pointure erronée");
        }
    }
    @Override
    public String toString() {
        return super.toString()+", pointure ="+this.pointure;
    }
}
