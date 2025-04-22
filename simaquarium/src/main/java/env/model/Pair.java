package env.model;

public class Pair<X, Y> {
    private X x;
    private Y y;

    public Pair(X x, Y y){
        this.x = x;
        this.y = y;
    }

    public X getX(){
        return this.x;
    }

    public Y getY(){
        return this.y;
    }

    public void setX(X newX){
        this.x= newX;
    }

    public void setY(Y newY){
        this.y= newY;
    }

    public static <X, Y> Pair<X, Y> of(X x, Y y){
        return new Pair<>(x, y);
    }
}
