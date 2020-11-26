package sample;

public class GameBoard {
    private GamePiece[][] board;

    public GameBoard(){
        board = new GamePiece[8][8];
    }

    public GamePiece[][] getBoard() {
        return board;
    }

    public void setBoard(GamePiece[][] board) {
        this.board = board;
    }
}
