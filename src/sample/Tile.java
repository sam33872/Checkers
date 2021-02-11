package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Stack;

public class Tile extends Rectangle {
    private GamePiece piece;
    private int x;
    private int y;
    private boolean hasAPieceTest;

    // Stack required to store all pieces that enter this piece during a run of the minimax algorithm
    // Allows for new pieces to be stored in order and removed back in the format of LIFO
    private Stack<GamePiece> oldPiece;

    /**
     * Constructor for Tile class
     * Extends rectangle so that tiles are displayed by 60x60 squares on screen
     * @param color - colour of the tile
     * @param x - x-coordinate of where this tile lays within the board structure
     * @param y - y-coordinate of where this tile lays within the board structure
     */
    public Tile(Color color, int x, int y){
        this.x = x;
        this.y = y;

        // Set actual position on screen to position in layout times by size of each tile
        setX(x * 60);
        setY(y * 60);

        // Set the width and height to a 60x60 square
        setWidth(60);
        setHeight(60);

        // Sets colour of tile to
        setFill(color);

        // Tile is first initiated without a tile
        piece = null;
        oldPiece = new Stack<GamePiece>();
        hasAPieceTest = false;
    }

    /**
     * Getter for y
     * @return y - y-coordinate of tile in board
     */
    public int returnY() {
        return y;
    }

    /**
     * Getter for x
     * @return x - x-coordinate of tile in board
     */
    public int returnX() {
        return x;
    }

    /**
     * Setter for piece
     * Updates hasAPieceTest to true if it has a piece, false if not
     * @param piece - A game piece to store in tile, null if to make empty
     */
    public void setPiece(GamePiece piece){
        this.piece = piece;
        if(piece == null){
            hasAPieceTest = false;
        }else{
            hasAPieceTest = true;
        }
    }

    /**
     * Add the piece that currently on the tile to the top of the stack
     * Used by minimax algorithm and successor function
     * Stores piece to be added back to tile when undoing moves
     */
    public void addToStack(){
        oldPiece.add(piece);
    }

    /**
     * Pops the top of the stack and sets the piece to the item removed from the stack
     * Used by the minimax algorithm and successor function
     */
    public void setToOldPiece() {
        this.piece = oldPiece.pop();
    }

    /**
     * Getter for piece within tile
     * @return - piece on this tile, null if no piece is on the tile
     */
    public GamePiece getPiece() {
        return piece;
    }

    /**
     * Checks if the tile has a piece, used to check if a piece can be moved here
     * @return - true if there is a piece on the tile, false otherwise
     */
    public boolean hasPiece() {
        if(piece == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Adds a orange stroke around a possible move
     */
    public void showAsPossibleMove(){
        setStroke(Color.ORANGE);
        setStrokeWidth(5);
    }

    /**
     * Adds a larger golden stroke around the optimal move
     */
    public void showAsBestPossibleMove(){
        setStroke(Color.GOLD);
        setStrokeWidth(8);
    }

    /**
     * Removes the stroke from the tile
     */
    public void unshowAsPossibleMove(){
        setStroke(null);
    }
}
