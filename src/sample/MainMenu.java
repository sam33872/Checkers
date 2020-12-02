package sample;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainMenu extends Application {
    private PieceBoard board;
    private TileBoard tileBoard;

    private Group grid;
    private Group pieces;
    private int turn;
    private int flag;

    private Color playerColor;
    private Color aiColor;

    public MainMenu(){
        board = new PieceBoard();
        tileBoard = new TileBoard();
        grid = new Group();
        pieces = new Group();
        turn = 0;
        flag = 0;
        aiColor = Color.BLUE;
    }

    @Override
    public void start(Stage mainStage) throws Exception{
        VBox vbox = prepareMenu(mainStage);
        Scene scene  = new Scene(vbox, 480 , 480);
        scene.setFill(Color.NAVY);
        mainStage.setResizable(false);
        mainStage.setTitle("Main Menu");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public VBox prepareMenu(Stage mainStage){
        // Title for main menu
        TextField title = new TextField("Main Menu");

        // Dropdown options box to pick difficulty wanted
        ComboBox difficulty = new ComboBox();
        difficulty.getItems().add("Easy");
        difficulty.getItems().add("Medium");
        difficulty.getItems().add("Hard");

        // Play button to enter game
        Button play = new Button("Play");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(difficulty.getValue() == null){
                    play.setText("Please Choose Difficulty");
                }
                else{
                    // Move to game screen
                    // Pass difficulty chosen
                    // change to game scene, let controller run game, return to menu
                    //Controller control = new Controller(board);
                    Scene gameScene = createScene();
                    mainStage.setScene(gameScene);
                    mainStage.show();

                }

            }
        });

        Button quit = new Button("Quit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.close();
            }
        });

        VBox vbox = new VBox(title, difficulty,play,quit);
        return vbox;
    }

    public void runAITurn(int d){
        // run ai turn
        int bestVal = -1;
        Tile bestMove = null;
        Tile endMove = null;
        flag = 0;
        PieceBoard pieceBoard = new PieceBoard();
        pieceBoard.setBoard(board.getBoard());

        TileBoard tempBoard = new TileBoard();
        tempBoard.setBoard(tileBoard.getBoard());
        int x = 0;
        int y = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(pieceBoard.getBoard()[j][i] != null){
                    if(pieceBoard.getBoard()[j][i].getColor() == aiColor){
                        ArrayList<Tile> posMoves = possibleMoves(tempBoard.getTile(j,i), tempBoard.getBoard());
                        for(int k = 0; k < posMoves.size(); k++){
                            int test = minimax(tempBoard.getTile(j,i),posMoves.get(k),d,"MAX", tempBoard.getBoard());
                            if(test > bestVal){
                                bestVal = test;
                                // bug with having variable here
                                x = j;
                                y = i;
                                endMove = posMoves.get(k);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(tileBoard.hasPiece(x,y));
        System.out.println("AAAAAAAAAA");
        for(int q = 0; q < 8; q++){
            for(int r = 0; r < 8; r++) {
                System.out.print(tileBoard.hasPiece(r,q));
                System.out.print(",");
            }
            System.out.println("");
        }
        if(endMove != null){
            GamePiece a = null;
            for(int i = 0; i < pieces.getChildren().size(); i++){
                GamePiece g = (GamePiece) pieces.getChildren().get(i);
                if(g.getX() == y && g.getY() == x){
                    a = g;
                }
            }
            a.relocate(10 + 60* endMove.returnX(), 10 + 60 * endMove.returnY());
            makeOffMove(y,x,endMove.returnX(),endMove.returnY());

            System.out.println("LOLOLOLOLOL");
            for(int q = 0; q < 8; q++){
                for(int r = 0; r < 8; r++) {
                    System.out.print(tileBoard.hasPiece(r,q));
                    System.out.print(",");
                }
                System.out.println("");
            }
        }
        turn = 0;
    }

    public int minimax(Tile s, Tile e, int d, String p, Tile[][] tempBoard){
        makeMove(tempBoard, s , e);
        if((d == 0) || (e.isLeaf() == true)){
            if(turn == 0){
                int eval = evaluate(Color.RED, tempBoard);
                unmakeMove(tempBoard,s,e);
                return eval;

            }else{
                int eval = evaluate(Color.BLUE, tempBoard);
                unmakeMove(tempBoard,s,e);
                return eval;
            }


        }
        ArrayList<Tile> posMoves = possibleMoves(e, tempBoard);
        if(p == "MAX"){
            int bestValue = -100;
            for(int i = 0; i < posMoves.size(); i++){
                int eval = minimax(e, posMoves.get(i),d-1, "MIN", tempBoard);
                if(eval > bestValue){
                    bestValue = eval;
                }
                unmakeMove(tempBoard,s,e);
            }
            return bestValue;
        }
        else{
            int bestValue = 100;
            for(int i = 0; i < posMoves.size(); i++){
                int eval = minimax(e, posMoves.get(i),d-1, "MAX", tempBoard);
                if(eval < bestValue){
                    bestValue = eval;
                }
                unmakeMove(tempBoard,s,e);
            }
            return bestValue;
        }
    }

    public void makeMove(Tile[][] board, Tile start, Tile end){
        int startX = start.returnX();
        int startY = start.returnY();
        int endX = end.returnX();
        int endY = end.returnY();

        if(startY - endY == -2){
            board[endX][endY].setPiece(board[startX][startY].getPiece());
            board[startX][startY].setPiece(null);
            board[startX-1][startY-1].setPiece(null);
        }
        else if(startY - endY == 2){
            board[endX][endY].setPiece(board[startX][startY].getPiece());
            board[startX][startY].setPiece(null);
        }

        if((startY - endY == -1) || (startY - endY == -1)){
            board[endX][endY].setPiece(board[startX][startY].getPiece());
            board[startX][startY].setPiece(null);
        }
    }

    public void unmakeMove(Tile[][] board, Tile start, Tile end){
        int startX = start.returnX();
        int startY = start.returnY();
        int endX = end.returnX();
        int endY = end.returnY();

        if(endY - startY == -2){
            board[startX][startY].setPiece(board[endX][endY].getPiece());
            board[endX][endY].setPiece(null);
            board[endX-1][endY-1].setPiece(null);
        }
        else if(endY - startY == 2){
            board[startX][startY].setPiece(board[endX][endY].getPiece());
            board[endX][endY].setPiece(null);
        }

        if((endY - startY == -1) || (endY - startY == -1)){
            board[startX][startY].setPiece(board[endX][endY].getPiece());
            board[endX][endY].setPiece(null);
        }
    }
    public void makeOffMove(int sX, int sY, int eX, int eY){
        tileBoard.setPiece(eX,eY,tileBoard.getPiece(sX,sY));
        tileBoard.setPiece(sX, sY, null);
    }

    public int evaluate(Color c, Tile[][] board){
        int value = 0;
        // red player
        if(c == Color.RED){
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(board[j][i].hasPiece() == true){
                        if(board[j][i].getPiece().getColor() == Color.RED){
                            value = value + 1;
                        }
                        else if(board[j][i].getPiece().getColor() == Color.BLUE){
                            value = value - 1;
                        }
                    }
                }
            }
        }
        // blue player
        else{
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(board[j][i].hasPiece() == true){
                        if(board[j][i].getPiece().getColor() == Color.RED){
                            value = value - 1;
                        }
                        else if(board[j][i].getPiece().getColor() == Color.BLUE){
                            value = value + 1;
                        }
                    }
                }
            }
        }

        return value;
    }

    public ArrayList<Tile> possibleMoves(Tile t, Tile[][] board){
        ArrayList<Tile> posMoves = new ArrayList<>();
        int currY = t.returnX();
        int currX = t.returnY();
        System.out.println(currX + " , " + currY);
        if (board[currX][currY].hasPiece() == true){
            if(board[currX][currY].getPiece().getKing() == true){

            }
            else {
                if ((currX + 1 <= 7) && (currY - 1 >= 0)) {
                    if (board[currX + 1][currY - 1].hasPiece() == false) {
                        posMoves.add(board[currX + 1][currY - 1]);
                    }
                } else {
                    if ((currX + 2 <= 7) && (currY - 2 >= 0)) {
                        if (board[currX + 2][currY - 2].hasPiece() == false) {
                            if(board[currX + 1][currY - 1].hasPiece() == false ||
                                    (board[currX + 1][currY - 1].hasPiece() == true && board[currX + 1][currY - 1].getPiece().getColor() == playerColor)) {
                                posMoves.add(board[currX + 2][currY - 2]);
                                flag = 1;
                            }
                        }
                    }
                }

                if ((currX - 1 >= 0) && (currY - 1 >= 0)) {
                    if (board[currX - 1][currY - 1].hasPiece() == false) {
                        posMoves.add(board[currX - 1][currY - 1]);
                    } else {
                        if ((currX - 2 >= 0) && (currY - 2 >= 0)) {
                            if (board[currX - 2][currY - 2].hasPiece() == false) {
                                if(board[currX - 1][currY - 1].hasPiece() == false ||
                                        (board[currX - 1][currY - 1].hasPiece() == true && board[currX - 1][currY - 1].getPiece().getColor() == playerColor)) {
                                    posMoves.add(board[currX - 2][currY - 2]);
                                    flag = 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        return posMoves;
    }

    /*public Tile takingMoves(Color color){
        int bestVal = 0;
        Tile takingMove;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(tileBoard[i][j].getPiece().getColor() == color){
                    flag = 0;
                    ArrayList<Tile> temp = possibleMoves(tileBoard[i][j]);
                    if(flag == 1){

                    }
                }
            }
        }

    }*/

    public void setupBoard(){
        playerColor = Color.RED;
        aiColor = Color.BLUE;
        // a keeps track of white or red tile
        int a = 0;
        // b keeps track of when the current row ends so that the next colour is opposite of first in row above
        int b = 0;

        // r will be used for references
        int r = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){

                if(a == 0){
                    Tile tile = new Tile(Color.WHITE,j,i);
                    // bug fix
                    grid.getChildren().add(tile);
                    tileBoard.setTile(j,i,tile);
                    a = 1;
                }
                else {
                    Tile tile = new Tile(Color.BLACK,j,i);
                    grid.getChildren().add(tile);
                    tileBoard.setTile(j,i,tile);
                    a = 0;
                }
                if(((i == 0) && (j % 2 != 0)) ||
                        ((i == 1) && (j % 2 == 0)) ||
                            ((i == 2) && (j % 2 != 0))){
                    GamePiece piece = createPiece(Color.RED,j,i);
                    piece.setReference("R" + r);
                    r++;
                    board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                if((i == 5 || i == 7) && (j == 0 || j == 2 || j == 4 || j == 6)){
                    GamePiece piece = createPiece(Color.BLUE,j,i);
                    piece.setReference("B" + r);
                    r++;
                    board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                else if((i == 6) && (j == 1 || j == 3 || j == 5 || j == 7)){
                    GamePiece piece = createPiece(Color.BLUE,j,i);
                    piece.setReference("B" + r);
                    r++;
                    board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                b++;
                if(b == 8) {
                    if (a == 0) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    b = 0;
                }
            }
        }
    }

    public GamePiece createPiece(Color color, int x, int y){
        GamePiece piece = new GamePiece(color,x,y);

        piece.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(turn == 0){
                    int oldX = piece.getX();
                    int oldY = piece.getY();

                    int newX = (int) mouseEvent.getSceneX();
                    int newY = (int) mouseEvent.getSceneY();

                    newX = newX / 60;
                    newY = newY / 60;

                    piece.relocate(10 + (newX * 60), 10 + (newY * 60));
                    updateBoard(oldX, oldY, newX, newY);
                    turn = 1;
                }
            }
        });

        return piece;
    }

    public void updateBoard(int oX, int oY, int nX, int nY){
        tileBoard.setPiece(nX,nY,tileBoard.getPiece(oX,oY));
        tileBoard.setPiece(oX,oY,null);
    }

    public Scene createScene(){
        Pane scene = new Pane();
        scene.setPrefSize(480,480);
        scene.getChildren().addAll(grid, pieces);

        setupBoard();

        Button endTurn = new Button("End Turn");
        endTurn.setOnAction(this::runAI);

        VBox vbox = new VBox(scene, endTurn);
        Scene gameScene = new Scene(vbox);
        return gameScene;
    }

    public void runAI(ActionEvent actionEvent){
        try {
            runAITurn(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        launch(args);
    }
}
