package sample;

public class TileBoard {
    private Tile[][] board;

    public TileBoard(){ board = new Tile[8][8]; }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public GamePiece getPiece(int i, int j){
        return board[i][j].getPiece();
    }

    public void setPiece(int i, int j, GamePiece piece){
        board[i][j].setPiece(piece);
    }

    public Tile getTile(int i, int j){
        return board[i][j];
    }

    public void setTile(int i, int j, Tile tile){
        board[i][j] = tile;
    }

    public Boolean hasPiece(int i, int j){
        return board[i][j].hasPiece();
    }
}
