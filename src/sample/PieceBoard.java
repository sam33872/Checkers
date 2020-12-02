package sample;

public class PieceBoard {
    private GamePiece[][] board;

    public PieceBoard(){
        board = new GamePiece[8][8];
    }

    public GamePiece[][] getBoard() {
        return board;
    }

    public void setBoard(GamePiece[][] board) {
        this.board = board;
    }

    public GamePiece getPiece(int i, int j){
        return board[i][j];
    }

    public Boolean hasPiece(int i, int j){ return board[i][j] != null;}

    public void setPiece(GamePiece piece, int i, int j){
        board[i][j] = piece;
    }
}
