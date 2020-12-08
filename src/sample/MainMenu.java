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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainMenu extends Application {
    //private PieceBoard board;
    private TileBoard tileBoard;

    private Group grid;
    private Group pieces;
    private int turn;
    private int flag;


    private int make;
    private int unmake;
    private int whatever;

    private Color playerColor;
    private Color aiColor;

    public MainMenu(){
        //board = new PieceBoard();
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
        System.out.println("AAAAAAAAAAAAA");
        for(int q = 0; q < 8; q++){
            for(int r = 0; r < 8; r++) {
                System.out.print(tileBoard.hasPiece(r,q));
                System.out.print(",");
            }
            System.out.println("");
        }

        make = 0;
        unmake = 0;

        // run ai turn
        int bestVal = -10000000;
        Tile bestMove = null;
        Tile endMove = null;
        flag = 0;
        whatever = 6890;

        int x = 0;
        int y = 0;
        int alpha = (int) Double.NEGATIVE_INFINITY;
        int beta = (int) Double.POSITIVE_INFINITY;

        System.out.println("Start ai check");
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(tileBoard.getPiece(j,i) != null){
                    if(tileBoard.getPiece(j,i).getColor() == aiColor){
                        //System.out.println("tile: " + j + "," + i);
                        ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), aiColor);
                        //System.out.println(posMoves.size());
                        for(int k = 0; k < posMoves.size(); k++){
                            make++;
                            makeMove(tileBoard.getTile(j,i), posMoves.get(k), aiColor);
                            int test = minimax(posMoves.get(k),d,alpha,beta,"MIN");
                            System.out.println("unmakeMove");;
                            unmake++;
                            unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), aiColor);
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

        if(endMove != null){
            /*GamePiece a = null;
            for(int i = 0; i < pieces.getChildren().size(); i++){
                GamePiece g = (GamePiece) pieces.getChildren().get(i);
                if(g.getX() == x && g.getY() == y){
                    a = g;
                }
            }
            System.out.println("Moved from: " + x + "," + y);
            System.out.println("Moved to: " + endMove.returnX() + "," + endMove.returnY());
            a.relocate(10 + 60* endMove.returnX(), 10 + 60 * endMove.returnY());
            a.setX(endMove.returnX());
            a.setY(endMove.returnY());*/
            makeOfficialMove(x,y,endMove.returnX(),endMove.returnY());

            System.out.println("CCCCCCCCCCCCCCCC");
            for(int q = 0; q < 8; q++){
                for(int r = 0; r < 8; r++) {
                   if(tileBoard.hasPiece(r,q)){
                       System.out.print(tileBoard.getPiece(r,q).getColor().hashCode());
                   }else {
                       System.out.print("false");
                   }
                   System.out.print(",");
                }
                System.out.println("");
            }

            System.out.println("Make: " + make);
            System.out.println("Unmake: " + unmake);
        }
    }

    public int minimax(Tile e, int d, int alpha, int beta, String maximise){
        if(whatever == 7000){
            System.out.println("CCCCCCCCCCCCCCCC");
            for(int q = 0; q < 8; q++){
                for(int r = 0; r < 8; r++) {
                    if(tileBoard.hasPiece(r,q)){
                        System.out.print(tileBoard.getPiece(r,q).getColor().hashCode());
                    }else {
                        System.out.print("false");
                    }
                    System.out.print(",");
                }
                System.out.println("");
            }
            whatever = 0;
        }else{
            whatever++;
        }
        //System.out.println("Minimax called");
        if((d == 0) || (e.isLeaf() == true)){
            if(maximise == "MAX"){
                return evaluate(Color.BLUE);
            }
            else{
                return evaluate(Color.RED);
            }
        }
        if(maximise == "MAX"){
            int bestValue = alpha;
            //System.out.println("Start ai check");
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++) {
                    if(tileBoard.getPiece(j,i) != null){
                        if(tileBoard.getPiece(j,i).getColor() == aiColor){
                            //System.out.println("tile: " + j + "," + i);
                            ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), aiColor);
                            for(int k = 0; k < posMoves.size(); k++){
                                make++;
                                makeMove(tileBoard.getTile(j,i), posMoves.get(k), aiColor);
                                bestValue = max(bestValue, minimax(posMoves.get(k),d-1, alpha, beta, "MIN"));
                                unmake++;
                                unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), aiColor);
                                alpha = max(alpha, bestValue);
                                if(alpha >= beta){
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return bestValue;
        }
        else{
            int bestValue = beta;
            //System.out.println("Start player check");
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++) {
                    if(tileBoard.getPiece(j,i) != null){
                        if(tileBoard.getPiece(j,i).getColor() == playerColor){
                            //System.out.println("tile: " + j + "," + i);
                            ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), playerColor);
                            for(int k = 0; k < posMoves.size(); k++){
                                make++;
                                makeMove(tileBoard.getTile(j,i), posMoves.get(k), playerColor);
                                bestValue = max(bestValue, minimax(posMoves.get(k),d-1, alpha, beta, "MAX"));
                                unmake++;
                                unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), playerColor);
                                beta = min(beta, bestValue);
                                if(alpha >= beta){
                                    break;
                                }
                             }
                        }
                    }
                }
            }
            return bestValue;
        }
    }

    public ArrayList<Tile> possibleMoves(Tile t, Color c){
        flag = 0;
        ArrayList<Tile> posMoves = new ArrayList<>();
        int currX = t.returnX();
        int currY = t.returnY();
        if(c == Color.RED){
            if (tileBoard.hasPiece(currX,currY) == true){
                if(tileBoard.getPiece(currX,currY).getKing() == true){
                    // IMPLEMENT IF KING
                }
                else {
                    // move 1 place to left
                    if ((currX - 1 >= 0) && (currY + 1 <= 7)) {
                        if (tileBoard.hasPiece(currX - 1,currY + 1) == false) {
                            posMoves.add(tileBoard.getTile(currX - 1,currY + 1));

                        }
                    } else {
                        // move 2 places to left
                        if ((currX - 2 >= 0) && (currY + 2 <= 7)) {
                            if (tileBoard.hasPiece(currX - 2,currY + 2) == false &&
                                    tileBoard.hasPiece(currX - 1,currY + 1) == true && tileBoard.getPiece(currX - 1,currY + 1).getColor() != Color.RED) {
                                posMoves.add(tileBoard.getTile(currX - 2,currY + 2));
                            }
                        }
                    }

                    // Move 1 place to right
                    if ((currX + 1 <= 7) && (currY + 1 <= 7)) {
                        if (tileBoard.hasPiece(currX + 1,currY + 1) == false) {
                            posMoves.add(tileBoard.getTile(currX + 1,currY + 1));
                        }
                        else {
                            // Move 2 places to right
                            if ((currX + 2 <= 7) && (currY + 2 <= 7)) {
                                if (tileBoard.hasPiece(currX + 2,currY + 2) == false &&
                                        tileBoard.hasPiece(currX + 1,currY + 1) == true && tileBoard.getPiece(currX + 1,currY + 1).getColor() != Color.RED){
                                    posMoves.add(tileBoard.getTile(currX + 2,currY + 2));
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(c == Color.BLUE){
            if (tileBoard.hasPiece(currX,currY) == true) {
                if (tileBoard.getPiece(currX, currY).getKing() == true) {
                    // IMPLEMENT IF KING
                } else {
                    // move 1 place to left
                    if ((currX + 1 <= 7) && (currY - 1 >= 0)) {
                        if (tileBoard.hasPiece(currX + 1, currY - 1) == false) {
                            posMoves.add(tileBoard.getTile(currX + 1, currY - 1));

                        }
                    } else {
                        // move 2 places to left
                        if ((currX + 2 <= 7) && (currY - 2 >= 0)) {
                            if (tileBoard.hasPiece(currX + 2, currY - 2) == false &&
                                    tileBoard.hasPiece(currX + 1, currY - 1) == true &&
                                        tileBoard.getPiece(currX + 1, currY - 1).getColor() == Color.RED) {
                                posMoves.add(tileBoard.getTile(currX + 2, currY - 2));
                            }
                        }
                    }

                    // Move 1 place to right
                    if ((currX - 1 >= 0) && (currY - 1 >= 0)) {
                        if (tileBoard.hasPiece(currX - 1, currY - 1) == false) {
                            posMoves.add(tileBoard.getTile(currX - 1, currY - 1));
                        } else {
                            // Move 2 places to right
                            if ((currX - 2 >= 0) && (currY - 2 >= 0)) {
                                if (tileBoard.hasPiece(currX - 2, currY - 2) == false &&
                                        tileBoard.hasPiece(currX - 1, currY - 1) == true && tileBoard.getPiece(currX - 1, currY - 1).getColor() != Color.BLUE) {
                                    posMoves.add(tileBoard.getTile(currX - 2, currY - 2));
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("current X: " + currX + ", current Y: " + currY);
        //System.out.println("posMoves: " + posMoves.size());
        return posMoves;
    }

    public void makeMove(Tile start, Tile end, Color c){
        int startX = start.returnX();
        int startY = start.returnY();
        int endX = end.returnX();
        int endY = end.returnY();
        if(c == Color.BLUE){
            if(startX - endX == -2){
                tileBoard.setPiece(endX,endY,tileBoard.getPiece(startX,startY));
                tileBoard.setPiece(startX,startY, null);
                tileBoard.getTile(startX+1, startY-1).addToStack(tileBoard.getPiece(startX + 1, startY-1));
                tileBoard.setPiece(startX+1, startY-1, null);
            }
            else if(startX - endX == 2){
                tileBoard.setPiece(endX,endY,tileBoard.getPiece(startX,startY));
                tileBoard.setPiece(startX,startY,null);
                tileBoard.getTile(startX-1, startY-1).addToStack(tileBoard.getPiece(startX - 1, startY-1));
                tileBoard.setPiece(startX-1,startY-1,null);
            }

            if((startX - endX == -1) || (startX - endX == 1)){
                tileBoard.setPiece(endX,endY,tileBoard.getPiece(startX,startY));
                tileBoard.setPiece(startX,startY,null);
            }
        }
        else if(c == Color.RED){
            if(startX - endX == 2){
                tileBoard.setPiece(endX, endY,tileBoard.getPiece(startX, startY));
                tileBoard.setPiece(startX,startY,null);
                tileBoard.getTile(startX-1, startY+1).addToStack(tileBoard.getPiece(startX - 1, startY+1));
                tileBoard.setPiece(startX-1, startY+1, null);

            }
            else if(startX - endX == -2){
                tileBoard.setPiece(endX, endY,tileBoard.getPiece(startX, startY));
                tileBoard.setPiece(startX,startY,null);
                tileBoard.getTile(startX+1, startY+1).addToStack(tileBoard.getPiece(startX + 1, startY+1));
                tileBoard.setPiece(startX+1, startY+1, null);
            }
            if((startX - endX == -1) || (startX - endX == 1)){
                tileBoard.setPiece(endX,endY,tileBoard.getPiece(startX,startY));
                tileBoard.setPiece(startX,startY,null);
            }
        }

    }

    public void unmakeMove(Tile start, Tile end, Color c){
        int startX = start.returnX();
        int startY = start.returnY();
        int endX = end.returnX();
        int endY = end.returnY();

        if(c == Color.BLUE){
            if(endX - startX == 2){
                tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
                tileBoard.setPiece(endX,endY,null);
                tileBoard.getTile(startX+1,startY-1).setToOldPiece();
            }
            else if(endX - startX == -2){
                tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
                tileBoard.setPiece(endX,endY,null);
                tileBoard.getTile(startX-1,startY-1).setToOldPiece();
            }
            if((endX - startX == -1) || (endX - startX == 1)){
                tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
                tileBoard.setPiece(endX,endY,null);
            }
        }
        else if(c == Color.RED){
            if(endX - startX == -2){
                tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
                tileBoard.setPiece(endX,endY,null);
                tileBoard.getTile(startX-1, startY+1).setToOldPiece();
            }
            else if(endX - startX == 2){
                tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
                tileBoard.setPiece(endX,endY,null);
                tileBoard.getTile(startX+1, startY+1).setToOldPiece();
            }
            if((endX - startX == 1) || (endX - startX == -1)){
                tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
                tileBoard.setPiece(endX,endY,null);
            }
        }
    }

    public void makeOfficialMove(int sX, int sY, int eX, int eY){
        GamePiece a = null;
        int b = 0;
        int findX = 0;
        int findY = 0;
        int take = 0;

        if(sX - eX == 2 && sY - eY == 2){
            findX = sX - 1;
            findY = sY - 1;
            take = 1;
        }
        else if(sX - eX == 2 && sY - eY == -2){
            findX = sX - 1;
            findY = sY + 1;
            take = 1;
        }
        else if(sX - eX == -2 && sY - eY == 2){
            findX = sX + 1;
            findY = sY - 1;
            take = 1;
        }
        else if(sX - eX == -2 && sY - eY == -2){
            findX = sX + 1;
            findY = sY + 1;
            take = 1;
        }

        for(int i = 0; i < pieces.getChildren().size(); i++){
            GamePiece g = (GamePiece) pieces.getChildren().get(i);
            if(g.getX() == sX && g.getY() == sY){
                a = g;
            }
            if(g.getX() == findX && g.getY() == findY){
                b = i;
            }
        }

        a.relocate(10 + 60* eX, 10 + 60 * eY);
        a.setX(eX);
        a.setY(eY);
        System.out.println(pieces.getChildren().size());
        if(take == 1){
            tileBoard.setPiece(findX, findY, null);
            pieces.getChildren().remove(b);
        }
        System.out.println(pieces.getChildren().size());
        tileBoard.setPiece(eX, eY, tileBoard.getPiece(sX,sY));
        tileBoard.setPiece(sX, sY, null);
    }

    public int evaluate(Color c){
        int value = 0;
        // red player
        if(c == Color.RED){
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(tileBoard.hasPiece(j,i) == true){
                        if(tileBoard.getPiece(j,i).getColor() == Color.RED){
                            value = value + 1;
                        }
                        else if(tileBoard.getPiece(j,i).getColor() == Color.BLUE){
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
                    if(tileBoard.hasPiece(j,i) == true){
                        if(tileBoard.getPiece(j,i).getColor() == Color.RED){
                            value = value - 1;
                        }
                        else if(tileBoard.getPiece(j,i).getColor() == Color.BLUE){
                            value = value + 1;
                        }
                    }
                }
            }
        }

        return value;
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
        playerColor = Color.BLUE;
        aiColor = Color.RED;
        System.out.println("111111111");
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
                    //board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                if((i == 5 || i == 7) && (j == 0 || j == 2 || j == 4 || j == 6)){
                    GamePiece piece = createPiece(Color.BLUE,j,i);
                    //board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                else if((i == 6) && (j == 1 || j == 3 || j == 5 || j == 7)){
                    GamePiece piece = createPiece(Color.BLUE,j,i);
                    //board.setPiece(piece,j,i);
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
        System.out.println("999999999999");
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

                    if(piece.getColor() == playerColor){
                        if(validMove(oldX, oldY, newX, newY) == true){
                            //piece.relocate(10 + (newX * 60), 10 + (newY * 60));
                            //piece.setX(newX);
                            //piece.setY(newY);
                            makeOfficialMove(oldX, oldY, newX, newY);
                            turn = 1;
                        }
                    }

                }
            }
        });

        return piece;
    }

    public Boolean validMove(int oldX, int oldY, int newX, int newY){
        boolean valid = false;
        ArrayList<Tile> validMoves = possibleMoves(tileBoard.getTile(oldX,oldY),playerColor);
        for(int i = 0; i < validMoves.size(); i++){
            if(validMoves.get(i) == tileBoard.getTile(newX, newY)){
                valid = true;
            }
        }
        return valid;
    }

    public Scene createScene(){
        Pane scene = new Pane();
        scene.setPrefSize(480,480);
        scene.getChildren().addAll(grid, pieces);

        setupBoard();
        Button endTurn = new Button("End Turn");
        endTurn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("222222222222222");
                runAITurn(1);
                turn = 0;
            }
        });
        VBox vbox = new VBox(scene, endTurn);
        Scene gameScene = new Scene(vbox);
        System.out.println("3333333333333");
        return gameScene;
    }

    public static void main(String args[]){
        launch(args);
    }
}
