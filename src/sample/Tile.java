package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Tile extends Rectangle {
    private GamePiece piece;
    private boolean leaf;
    private int x;
    private int y;

    public Tile(Color color, int x, int y){
        this.x = x;
        this.y = y;
        setX(x * 60);
        setY(y * 60);
        setWidth(60);
        setHeight(60);
        setFill(color);
        piece = null;
        if((x == 0) || (x == 7) ||
                (y == 0) || (y == 7)){
            leaf = true;
        }
        else{ leaf = false; }
    }

    public int returnY() {
        return y;
    }

    public int returnX() {
        return x;
    }

    public void setPiece(GamePiece piece){
        this.piece = piece;
    }

    public GamePiece getPiece() {
        return piece;
    }

    public boolean hasPiece() {
        if(piece == null){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isLeaf() {
        return leaf;
    }
}
