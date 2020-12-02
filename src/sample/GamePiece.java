package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GamePiece extends Circle {
    private String reference;
    private Boolean king;
    private Color color;
    private int x;
    private int y;

    public GamePiece(Color color, int x, int y){
        king = false;
        this.x = x;
        this.y = y;
        this.color = color;
        setCenterX(30 + (x * 60));
        setCenterY(30 + (y * 60));
        setRadius(20);
        setFill(color);
    }
    public void setReference(String reference){
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setKing(Boolean king) {
        this.king = king;
    }

    public Boolean getKing() {
        return king;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
