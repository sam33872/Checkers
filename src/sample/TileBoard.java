package sample;

public class TileBoard {
    private Tile[][] board;

    /**
     * Constructor initialises a 8x8 game board
     */
    public TileBoard(){ board = new Tile[8][8]; }

    /**
     * Getter for piece on tile
     * @param i - x-coordinate of tile, to access piece on board
     * @param j - y-coordinate of tile, to access piece on board
     * @return piece on the tile board in position specified
     */
    public GamePiece getPiece(int i, int j){
        return board[i][j].getPiece();
    }

    /**
     * Setter for piece on tile
     * @param i - x-coordinate of tile, to access piece on board
     * @param j - y-coordinate of tile, to access piece on board
     * @param piece - piece to add to the board in position specified
     */
    public void setPiece(int i, int j, GamePiece piece){
        board[i][j].setPiece(piece);
    }

    /**
     * Getter for the tile
     * @param i - x-coordinate of tile wanted
     * @param j - y-coordinate of tile wanted
     * @return tile in position specified
     */
    public Tile getTile(int i, int j){
        return board[i][j];
    }

    /**
     * Setter for tile
     * @param i - x-coordinate of tile to set
     * @param j - y-coordinate of tile to set
     * @param tile - tile to set to piece in the board of tiles
     */
    public void setTile(int i, int j, Tile tile){
        board[i][j] = tile;
    }

    /**
     * Check if a tile in the board has a piece
     * @param i x-coordinate of tile to check
     * @param j - y-coordinate of tile to check
     * @return true if there is a piece on the tile specified, false otherwise
     */
    public Boolean hasPiece(int i, int j){
        return board[i][j].hasPiece();
    }
}
