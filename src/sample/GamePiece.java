package sample;

public class GamePiece {
    private Boolean king;

    public GamePiece(){
        king = false;
    }

    public void setKing(Boolean king) {
        this.king = king;
    }

    public Boolean getKing() {
        return king;
    }
}
