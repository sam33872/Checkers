package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GamePiece extends Circle {
    private Boolean king;
    private final Color color;
    private int x;
    private int y;

    /**
     * Constructor for GamePiece class
     * Extends Circle to allow for my game pieces to be displayed as circles
     * @param color - colour of the tile, based on which player it belongs to
     * @param x - initial x-coordinate on board
     * @param y - initial y-coordinate on board
     */
    public GamePiece(Color color, int x, int y){
        king = false;
        this.x = x;
        this.y = y;
        this.color = color;
        // Aligns to correct position on centre of the correct tile
        setCenterX(30 + (x * 60));
        setCenterY(30 + (y * 60));
        setRadius(20);
        // Sets the colour of the piece to visually show who it belongs to
        setFill(color);
    }

    /**
     * Makes the piece a king and visually changes piece to show that it is a king by adding a golden edge around outside
     */
    public void makeKing() {
        // Set king variable to true
        this.king = true;

        // Add stroke effect to piece to represent as a king
        setStroke(Color.GOLD);
    }

    /**
     * Sets king to false
     * Used for undoing minimax algorithm moves
     */
    public void removeKing(){
        this.king = false;
    }

    /**
     * Getter for king variable, used to check if this piece is currently a king
     * @return king - true if this piece is currently a king, false otherwise
     */
    public Boolean getKing() {
        return king;
    }

    /**
     * Getter for colour of piece
     * @return color - either red or blue depending on piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter for x
     * @return x - x-coordinate of position in grid (relative to board not display screen)
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y
     * @return y - y-coordinate of position in grid
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for x
     * @param x - x-coordinate of position on board (relative to board not display)
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter for y
     * @param y - y-coordinate of position on board
     */
    public void setY(int y) {
        this.y = y;
    }
}
