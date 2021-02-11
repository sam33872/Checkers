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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Controller extends Application {
    private TileBoard tileBoard;

    private Group grid;
    private Group pieces;
    private int turn;
    private int flag;

    private Color playerColor;
    private Color aiColor;

    private Boolean hintActive;
    private ArrayList<Tile> hintPossMoves;
    private Tile hintBestMove;

    private TextField commandOutput;

    public Controller(){
        tileBoard = new TileBoard();
        grid = new Group();
        pieces = new Group();
        turn = 0;
        flag = 0;
        hintActive = false;
        hintPossMoves = new ArrayList<>();
        hintBestMove = null;
    }

    /**
     * Default function from javaFX that is used to initial the starting scene
     * Calls a method to prepare menu and sets the suitable sizes and settings of the scene
     * @param mainStage - default parameter that is used to display created scene
     * @throws Exception - stops errors occurring by potentially throwing exception in certain areas
     */
    @Override
    public void start(Stage mainStage) throws Exception{
        // Prepares the scene by calling a function to create a menu
        VBox vbox = prepareMenu(mainStage);
        vbox.setSpacing(25);
        // CSS styling of menu
        vbox.setStyle(" -fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-alignment: center; ");
        Scene scene  = new Scene(vbox, 480 , 480);
        mainStage.setResizable(false);
        mainStage.setTitle("Main Menu");
        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * Creates the main menu screen
     * Including a button to start game, a drop-down for difficulty levels and a button for a rules pop-up menu
     * @param mainStage - stage is passed through to allow for pop-up menu to display and for game scene to run
     * @return returns vbox containing menu to be used for initial scene
     */
    public VBox prepareMenu(Stage mainStage){
        // Title for main menu
        Text title = new Text("Main Menu");
        title.setFont(Font.font(24));
        title.setUnderline(true);
        title.setStroke(Color.GOLD);

        // Pop-up box for rules of the game
        Button rules = new Button("Rules");
        // On click handler to manage event of clicking the button
        rules.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Creates a new pop-up box and sets height and width
                Popup rulesPopup = new Popup();
                rulesPopup.setWidth(400);
                rulesPopup.setHeight(600);
                // Creates a title for pop-up menu
                Text titleOfPopup= new Text("Rules of the game:");
                // Sets size of title to font size 24
                titleOfPopup.setFont(Font.font(24));
                // Adds a gold stroke and underline to make title stand out
                titleOfPopup.setStroke(Color.GOLD);
                titleOfPopup.setUnderline(true);
                // A text is created to hold all the rules of the game
                Text allRules = new Text("1: Red always goes first\n2: Each player has 12 pieces" +
                                        "\n3: Regular pieces can only move forwards diagonally\n4: Kings can move any direction diagonally" +
                                        "\n5: A piece becomes a king once it reaches the opposite end of the grid\n6: Pieces can only move 1 space forwards unless completing a capture" +
                                        "\n7: Both players must complete a capture if one is available, but can pick which capture to take if multiple are available" +
                                        "\n8: Player can capture multiple pieces in one go if they are available by that same piece in continuous move" +
                                        "\n9: If a player takes a piece and reaches the end to become a king, it cannot capture another piece even if they are available" +
                                        "\n10: The game is won when all of the opponents pieces have been captured");
                // Sets size of rules to font size 16
                allRules.setFont(Font.font(16));
                // Created a button to close rules pop-up
                Button closeRules = new Button("Close Rules");
                // Added on click handler to close pop-up screen once clicked
                closeRules.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        rulesPopup.hide();
                    }
                });
                // Used a VBox with spacing and CSS styling to create a visually suitable pop-up
                VBox items = new VBox(titleOfPopup,allRules,closeRules);
                items.setSpacing(20);
                items.setStyle(" -fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-alignment: center; ");
                rulesPopup.getContent().addAll(items);
                // Displays pop-up to screen
                rulesPopup.show(mainStage);
            }
        });

        // Dropdown options box to pick difficulty wanted
        ComboBox difficulty = new ComboBox();
        difficulty.getItems().add("Easy");
        difficulty.getItems().add("Medium");
        difficulty.getItems().add("Hard");

        // Dropdown options box for which colour player
        // Also consists of a random option that will randomly pick which colour the player is
        ComboBox playerColour = new ComboBox();
        playerColour.getItems().add("Red");
        playerColour.getItems().add("Blue");
        playerColour.getItems().add("Random");

        // Play button to enter game
        Button play = new Button("Play");
        // On click handler to start game scene
        play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // If no difficulty has been selected in drop down menu then error message outputted in text field
                if(difficulty.getValue() == null){
                    commandOutput.setText("Please Choose Difficulty");
                }
                // If no colour has been picked, outputs an error
                else if(playerColour.getValue() == null){
                    commandOutput.setText("Please Choose Colour");
                }
                else{
                    // Sets up game screen including the board and pieces
                    Scene gameScene = createScene(difficulty.getValue().toString(), playerColour.getValue().toString(), mainStage);
                    // Move to game screen, passing difficulty selected
                    mainStage.setScene(gameScene);
                    mainStage.show();

                }

            }
        });

        // Button to exit the game
        Button quit = new Button("Quit");
        // On click handler that closes stage (program)
        quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mainStage.close();
            }
        });

        // Sets text in field to starting message
        commandOutput = new TextField("Output: ");

        // Constructs box to hold all the options in a vertical order
        VBox vbox = new VBox(title, rules, difficulty, playerColour, play,quit, commandOutput);
        return vbox;
    }

    /**
     * Main function for running the ai turn and making a move
     * @param d - the depth that the minimax algorithm goes to
     * @throws InterruptedException - stops error caused by calling this function
     */
    public void runAITurn(int d) throws InterruptedException {
        // Initialise bestVal to very small number
        int bestVal = -10000000;

        // Empty/Blank variables to store coordinate/tile of the two tiles to move between
        Tile endMove = null;
        int x = 0;
        int y = 0;

        // flag to catch if a capture is required
        flag = 0;

        //Initialise alpha to negative infinity and beta to positive infinity
        int alpha = (int) Double.NEGATIVE_INFINITY;
        int beta = (int) Double.POSITIVE_INFINITY;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                // Checks if this position on board has a piece
                if(tileBoard.getPiece(j,i) != null){
                    // Checks if this piece is an ai piece
                    if(tileBoard.getPiece(j,i).getColor() == aiColor){
                        // Gets all the valid moves available for this piece
                        ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), aiColor);
                        if(flag == 0){
                            for(int k = 0; k < posMoves.size(); k++){
                                // if the capture flag has not be caught yet, check for an captures
                                if(posMoves.get(k).returnX() - j == 2 || posMoves.get(k).returnX() - j == -2){
                                    // set move to be to this piece to force a capture
                                    // changing flag will stop this function from running again
                                    // but flag will stop non capturing moves from becoming endMove
                                    flag = 1;
                                    x = j;
                                    y = i;
                                    endMove = posMoves.get(k);
                                }
                            }
                        }
                        // Loop through all the possible moves for this piece
                        for(int k = 0; k < posMoves.size(); k++){
                            // temporarily make the move from tile j,i to possible move
                            makeMove(tileBoard.getTile(j,i), posMoves.get(k), aiColor);
                            // check if piece needs to become king, if this is the case it makes it a king
                            // returns true if this happens, false if not
                            Boolean king = checkForKing(posMoves.get(k), aiColor);
                            // Calls minimax function, with new position of tile, depth, alpha, beta, both players colours (current player first) and maximise
                            int test = minimax(posMoves.get(k),d,alpha,beta, aiColor, playerColor, "MAX");
                            // Unmakes move made before minimax call
                            unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), aiColor);
                            if(king == true){
                                // If piece was changed to a king, removes that king
                                tileBoard.getTile(j,i).getPiece().removeKing();
                            }
                            if(test > bestVal){
                                // Checks if capture flag has been triggered
                                if(flag == 1){
                                    // if flag is true, only change position to another better capture
                                    if(posMoves.get(k).returnX() - j == 2 || posMoves.get(k).returnX() - j == -2){
                                        x = j;
                                        y = i;
                                        endMove = posMoves.get(k);
                                    }
                                // if flag is false, set to better option regardless of being a capture or not
                                }else{
                                    // Updates value of bestVal to best value found
                                    bestVal = test;
                                    x = j;
                                    y = i;
                                    endMove = posMoves.get(k);
                                }

                            }
                        }
                    }
                }
            }
        }
        // Validates that an move was found
        if(endMove != null){
            // Check that the game hasn't ended
            if(gameWon() == null){
                // Complete the actual move from x,y to endMove
                int king = makeOfficialMove(x,y,endMove.returnX(),endMove.returnY(), aiColor);
                int previousX = endMove.returnX();
                int previousY = endMove.returnY();

                // If a king was made in makeOfficalMove, do not allow piece to move again
                if(king == 1){
                    flag = 0;
                }

                // If a capture occurred
                if(flag == 1) {
                    int test = 0;
                    while(test == 0){
                        // Check if there is another checker it can capture straight away
                        Tile take = checkDoubleTake(endMove, aiColor);
                        if(take != null){
                            // If there is take it
                            // Don't end loop and look for another capture
                            makeOfficialMove(previousX,previousY,take.returnX(),take.returnY(), aiColor);
                            previousX = take.returnX();
                            previousY = take.returnY();
                        }
                        else{
                            // Else end loop and turn
                            test = 1;
                            turn = 0;
                        }
                    }
                }
            }
            else{
                // Error message if you try to play ai turn and it has already won
                System.out.println("Game already won!");
            }
        }
    }

    /**
     * Method to check if a piece can capture another piece
     * Used after one capture has been already made
     * @param t - the tile the piece is currently on
     * @param c - colour of current player's pieces
     * @return a tile it can capture, if not capture available returns null
     */
    public Tile checkDoubleTake(Tile t, Color c){
        // Set to null to begin with so that if no capture found returns it
        Tile nextTake = null;
        ArrayList<Tile> possMoves = possibleMoves(t,c);
        for(int i = 0; i < possMoves.size(); i++){
            if((possMoves.get(i).returnX() - t.returnX() == -2) || (possMoves.get(i).returnX() - t.returnX() == 2)){
                // If capture found, sets nextTake to the tile it needs to move to
                nextTake = possMoves.get(i);
                // Set i to above limit to end search
                i = 5;
            }
        }
        // returns till to move to or null
        return nextTake;
    }

    /**
     * Minimax algorithm used to calculate to find heuristic values for possible routes
     * The goal is to find the best possible route d moves (including opponent moves) down the line
     * @param e - the most recent move position
     * @param d - amount of depth left to search
     * @param alpha - alpha value used for alpha-beta pruning
     * @param beta - beta value used for alpha-beta pruning
     * @param player - current player's colour
     * @param opponent - opponent player's colour
     * @param maximise - MAX to maximise or MIN to minimise
     * @return heuristic value to represent value of path
     */
    public int minimax(Tile e, int d, int alpha, int beta, Color player, Color opponent, String maximise){
        // Checks if at maximum depth
        if((d == 0)){
            // Returns heuristic value depending on whether maximising or minimising
            if(maximise == "MAX"){
                return evaluate(player);
            }
            else{
                return evaluate(opponent);
            }
        }

        // Checks if game has been won
        Color lost = gameWon();
        // If game is lost by current player then returns very bad heuristic value
        if((lost == opponent && maximise == "MAX") || (lost == player && maximise == "MIN")){
            return -10000;
        }
        // If game is lost by opponent then returns very high heuristic value
        else if((lost == player && maximise == "MAX") || (lost == opponent && maximise == "MIN")){
            return 10000;
        }

        // If maximising
        if(maximise == "MAX"){
            // Initialise bestValue to alpha
            int bestValue = alpha;
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++) {
                    // Goes through the board looking for pieces and finds pieces of its kind
                    if(tileBoard.getPiece(j,i) != null){
                        if(tileBoard.getPiece(j,i).getColor() == player){
                            // Checks for moves the current player could make in this scenario
                            ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), player);
                            for(int k = 0; k < posMoves.size(); k++){
                                // Make this temporary move and do king check
                                makeMove(tileBoard.getTile(j,i), posMoves.get(k), player);
                                Boolean king = checkForKing(posMoves.get(k), player);
                                // Recursively calls minimax function again with new position, depth decreased by 1 and changes from maximise to minimise
                                bestValue = max(bestValue, minimax(posMoves.get(k),d-1, alpha, beta, player, opponent, "MIN"));
                                // Removes the temporary move and undo king check
                                unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), player);
                                if(king == true){
                                    tileBoard.getTile(j,i).getPiece().removeKing();
                                }
                                // Update alpha to larger value
                                alpha = max(alpha, bestValue);
                                if(alpha >= beta){
                                    // If alpha is bigger than or equal to beta, return alpha
                                    return alpha;
                                }
                            }
                        }
                    }
                }
            }
            // Return best value found (biggest)
            return bestValue;
        }
        // If minimising
        else{
            // Initialises bestValue to beta
            int bestValue = beta;
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++) {
                    // Goes through the board looking for pieces and finds pieces of opponents kind
                    if(tileBoard.getPiece(j,i) != null){
                        if(tileBoard.getPiece(j,i).getColor() == opponent){
                            // Checks for moves the current player could make in this scenario
                            ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), opponent);
                            for(int k = 0; k < posMoves.size(); k++){
                                // Make this temporary move and do king check
                                makeMove(tileBoard.getTile(j,i), posMoves.get(k), opponent);
                                Boolean king = checkForKing(posMoves.get(k), opponent);
                                // Recursively calls minimax function again with new position, depth decreased by 1 and changes from minimise to maximise
                                // Sets value of bestValue to smaller
                                bestValue = min(bestValue, minimax(posMoves.get(k),d-1, alpha, beta, player, opponent, "MAX"));
                                // Removes this temporary move and undo king check
                                unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), opponent);
                                if(king == true){
                                    tileBoard.getTile(j,i).getPiece().removeKing();
                                }
                                // Update beta to the smaller value
                                beta = min(beta, bestValue);
                                if(alpha >= beta){
                                    // If alpha is bigger than or equal to beta, return beta
                                    return beta;
                                }
                             }
                        }
                    }
                }
            }
            // Return bestValue found (smallest)
            return bestValue;
        }
    }

    /**
     * Function to check if the game has been won
     * @return colour of player that has lost, null if noone has won yet
     */
    public Color gameWon(){
        // Setup counters for both players
        int blue = 0;
        int red = 0;

        // Runs through all the pieces on the board using the group
        for(int i = 0; i < pieces.getChildren().size(); i++){
            GamePiece piece = (GamePiece) pieces.getChildren().get(i);
            if(piece.getColor() == Color.BLUE){
                // If blue piece found, increment blue count
                blue++;
            }
            else{
                // If red piece found, increment red count
                red++;
            }
        }
        // If no blue found then blue player has lost, so return blue
        if(blue == 0){
            return Color.BLUE;
        }
        // If no red found then blue player has lost, so return red
        if(red == 0){
            return Color.RED;
        }
        // If noone has lost then return null
        return null;
    }

    /**
     * Method to find all the valid positions a piece can move to
     * @param t - the current tile that a piece is on
     * @param c - the colour of the current player
     * @return an arraylist of all the possible valid tiles the piece could move
     */
    public ArrayList<Tile> possibleMoves(Tile t, Color c){
        // Setup a new ArrayList to store tiles
        ArrayList<Tile> posMoves = new ArrayList<>();

        // Get x and y values from tile for easier use in if tests
        int currX = t.returnX();
        int currY = t.returnY();

        // If red player
        if(c == Color.RED) {
            // Check that piece exists in this tile
            if (tileBoard.hasPiece(currX, currY) == true) {
                // Check the position diagonally left and down by 1 space
                if ((currX - 1 >= 0) && (currY + 1 <= 7)) {
                    if (tileBoard.hasPiece(currX - 1, currY + 1) == false) {
                        posMoves.add(tileBoard.getTile(currX - 1, currY + 1));

                    } else {
                        // Check the position diagonally left and down by 2 space
                        if ((currX - 2 >= 0) && (currY + 2 <= 7)) {
                            // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                            if (tileBoard.hasPiece(currX - 2, currY + 2) == false &&
                                    tileBoard.hasPiece(currX - 1, currY + 1) == true && tileBoard.getPiece(currX - 1, currY + 1).getColor() != Color.RED) {
                                posMoves.add(tileBoard.getTile(currX - 2, currY + 2));
                            }
                        }
                    }
                }
                // Check the position diagonally right and down by 1 space
                if ((currX + 1 <= 7) && (currY + 1 <= 7)) {
                    if (tileBoard.hasPiece(currX + 1, currY + 1) == false) {
                        posMoves.add(tileBoard.getTile(currX + 1, currY + 1));
                    } else {
                        // Check the position diagonally right and down by 2 space
                        if ((currX + 2 <= 7) && (currY + 2 <= 7)) {
                            // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                            if (tileBoard.hasPiece(currX + 2, currY + 2) == false &&
                                    tileBoard.hasPiece(currX + 1, currY + 1) == true && tileBoard.getPiece(currX + 1, currY + 1).getColor() != Color.RED) {
                                posMoves.add(tileBoard.getTile(currX + 2, currY + 2));
                            }
                        }
                    }
                }
            }
        }
        // For blue player
        else if (c == Color.BLUE) {
            if (tileBoard.hasPiece(currX, currY) == true) {
                // Check the position diagonally right and up by 1 space
                if ((currX + 1 <= 7) && (currY - 1 >= 0)) {
                    if (tileBoard.hasPiece(currX + 1, currY - 1) == false) {
                        posMoves.add(tileBoard.getTile(currX + 1, currY - 1));
                    } else {
                        // Check the position diagonally right and up by 2 space
                        if ((currX + 2 <= 7) && (currY - 2 >= 0)) {
                            // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                            if (tileBoard.hasPiece(currX + 2, currY - 2) == false &&
                                    tileBoard.hasPiece(currX + 1, currY - 1) == true &&
                                    tileBoard.getPiece(currX + 1, currY - 1).getColor() == Color.RED) {
                                posMoves.add(tileBoard.getTile(currX + 2, currY - 2));
                            }
                        }
                    }
                }
                // Check the position diagonally left and up by 1 space
                if ((currX - 1 >= 0) && (currY - 1 >= 0)) {
                    if (tileBoard.hasPiece(currX - 1, currY - 1) == false) {
                        posMoves.add(tileBoard.getTile(currX - 1, currY - 1));
                    } else {
                        // Check the position diagonally left and up by 2 space
                        if ((currX - 2 >= 0) && (currY - 2 >= 0)) {
                            // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                            if (tileBoard.hasPiece(currX - 2, currY - 2) == false &&
                                    tileBoard.hasPiece(currX - 1, currY - 1) == true && tileBoard.getPiece(currX - 1, currY - 1).getColor() != Color.BLUE) {
                                posMoves.add(tileBoard.getTile(currX - 2, currY - 2));
                            }
                        }
                    }
                }
            }
        }
        // If a piece is a king, then it has more checks here
        if (tileBoard.hasPiece(currX, currY) == true) {
            if (tileBoard.getPiece(currX, currY).getKing() == true) {
                // If it is a blue king
                if (c == Color.BLUE) {
                    // Check the position diagonally left and down by 1 space
                    if ((currX - 1 >= 0) && (currY + 1 <= 7)) {
                        if (tileBoard.hasPiece(currX - 1, currY + 1) == false) {
                            posMoves.add(tileBoard.getTile(currX - 1, currY + 1));

                        } else {
                            // Check the position diagonally left and down by 2 space
                            if ((currX - 2 >= 0) && (currY + 2 <= 7)) {
                                // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                                if (tileBoard.hasPiece(currX - 2, currY + 2) == false &&
                                        tileBoard.hasPiece(currX - 1, currY + 1) == true &&
                                            tileBoard.getPiece(currX - 1, currY + 1).getColor() != Color.BLUE) {
                                    posMoves.add(tileBoard.getTile(currX - 2, currY + 2));
                                }
                            }
                        }
                    }
                    // Check the position diagonally right and down by 1 space
                    if ((currX + 1 <= 7) && (currY + 1 <= 7)) {
                        if (tileBoard.hasPiece(currX + 1, currY + 1) == false) {
                            posMoves.add(tileBoard.getTile(currX + 1, currY + 1));
                        } else {
                            // Check the position diagonally right and down by 2 space
                            if ((currX + 2 <= 7) && (currY + 2 <= 7)) {
                                // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                                if (tileBoard.hasPiece(currX + 2, currY + 2) == false &&
                                        tileBoard.hasPiece(currX + 1, currY + 1) == true &&
                                            tileBoard.getPiece(currX + 1, currY + 1).getColor() != Color.BLUE) {
                                    posMoves.add(tileBoard.getTile(currX + 2, currY + 2));
                                }
                            }
                        }
                    }
                }
                // If red king
                else if (c == Color.RED) {
                    if (tileBoard.hasPiece(currX, currY) == true) {
                        // Check the position diagonally right and up by 1 space
                        if ((currX + 1 <= 7) && (currY - 1 >= 0)) {
                            if (tileBoard.hasPiece(currX + 1, currY - 1) == false) {
                                posMoves.add(tileBoard.getTile(currX + 1, currY - 1));
                            } else {
                                // Check the position diagonally right and up by 2 space
                                if ((currX + 2 <= 7) && (currY - 2 >= 0)) {
                                    // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                                    if (tileBoard.hasPiece(currX + 2, currY - 2) == false &&
                                            tileBoard.hasPiece(currX + 1, currY - 1) == true &&
                                                tileBoard.getPiece(currX + 1, currY - 1).getColor() != Color.RED) {
                                        posMoves.add(tileBoard.getTile(currX + 2, currY - 2));
                                    }
                                }
                            }
                        }
                    }
                    // Check the position diagonally left and up by 1 space
                    if ((currX - 1 >= 0) && (currY - 1 >= 0)) {
                        if (tileBoard.hasPiece(currX - 1, currY - 1) == false) {
                            posMoves.add(tileBoard.getTile(currX - 1, currY - 1));
                        } else {
                            // Check the position diagonally left and up by 2 space
                            if ((currX - 2 >= 0) && (currY - 2 >= 0)) {
                                // Checks that there is a piece between old and new tiles and that this piece was an opponent piece
                                if (tileBoard.hasPiece(currX - 2, currY - 2) == false &&
                                        tileBoard.hasPiece(currX - 1, currY - 1) == true &&
                                            tileBoard.getPiece(currX - 1, currY - 1).getColor() != Color.RED) {
                                    posMoves.add(tileBoard.getTile(currX - 2, currY - 2));
                                }
                            }
                        }
                    }
                }
            }
        }
        // return ArrayList of all the possible moves
        return posMoves;
    }

    /**
     * Function used to make a temporary move by the minimax function and successor functions
     * These moves are only in the back end and not visual
     * @param start - the tile the piece is currently on
     * @param end - the tile the piece is moving to
     * @param c - which player is moving, used if taking a piece
     */
    public void makeMove(Tile start, Tile end, Color c){
        // Extract x and y of tiles for easier use in if statement
        int startX = start.returnX();
        int startY = start.returnY();
        int endX = end.returnX();
        int endY = end.returnY();

        // For each possible diagonal direction that a capture can be made
        // Sets piece to end tile
        // Removes piece from start tile and removes piece in-between
        // In-between piece is added to a stack within the tile for recovery in unmaking moves
        if (startX - endX == -2 && startY - endY == -2) {
            tileBoard.setPiece(endX, endY, tileBoard.getPiece(startX, startY));
            tileBoard.setPiece(startX, startY, null);
            tileBoard.getTile(startX + 1, startY + 1).addToStack();
            tileBoard.setPiece(startX + 1, startY + 1, null);
        }
        else if(startX - endX == -2 && startY - endY == 2){
            tileBoard.setPiece(endX, endY, tileBoard.getPiece(startX, startY));
            tileBoard.setPiece(startX, startY, null);
            tileBoard.getTile(startX + 1, startY - 1).addToStack();
            tileBoard.setPiece(startX + 1, startY - 1, null);
        }
        else if (startX - endX == 2 && startY - endY == -2) {
            tileBoard.setPiece(endX, endY, tileBoard.getPiece(startX, startY));
            tileBoard.setPiece(startX, startY, null);
            tileBoard.getTile(startX - 1, startY + 1).addToStack();
            tileBoard.setPiece(startX - 1, startY + 1, null);
        }
        else if(startX - endX == 2 && startY - endY == 2){
            tileBoard.setPiece(endX, endY, tileBoard.getPiece(startX, startY));
            tileBoard.setPiece(startX, startY, null);
            tileBoard.getTile(startX - 1, startY - 1).addToStack();
            tileBoard.setPiece(startX - 1, startY - 1, null);
        }
        // If the piece only moves diagonally by 1
        // Piece is set to end tile
        // Then piece is removed from start tile
        if((startX - endX == -1) || (startX - endX == 1)){
            tileBoard.setPiece(endX,endY,tileBoard.getPiece(startX,startY));
            tileBoard.setPiece(startX,startY,null);
        }
    }

    /**
     * Method to undo the changes made by makeMove
     * Recovers captured pieces back to board from stack
     * @param start - tile from where the move had started
     * @param end - tile from where the move had finished
     * @param c - colour of the current player
     */
    public void unmakeMove(Tile start, Tile end, Color c){
        // Extract x and y of tiles for easier use in if statement
        int startX = start.returnX();
        int startY = start.returnY();
        int endX = end.returnX();
        int endY = end.returnY();

        // For each possible diagonal direction that a capture was made
        // Resets piece to start tile from end tile
        // Removes piece from end tile and recovers piece in-between
        // In-between piece is popped from the stack within the tile
        if(startX - endX == -2 && startY - endY == -2){
            tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
            tileBoard.setPiece(endX,endY,null);
            tileBoard.getTile(startX + 1, startY + 1).setToOldPiece();
        }
        else if(startX - endX == -2 && startY - endY == 2){
            tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
            tileBoard.setPiece(endX,endY,null);
            tileBoard.getTile(startX + 1, startY - 1).setToOldPiece();
        }
        if(startX - endX == 2 && startY - endY == -2) {
            tileBoard.setPiece(startX, startY, tileBoard.getPiece(endX, endY));
            tileBoard.setPiece(endX, endY, null);
            tileBoard.getTile(startX - 1, startY + 1).setToOldPiece();
        }
        else if(startX - endX == 2 && startY - endY == 2){
            tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
            tileBoard.setPiece(endX,endY,null);
            tileBoard.getTile(startX - 1, startY - 1).setToOldPiece();
        }
        // If the piece only moves diagonally by 1
        // Piece is set back to start tile
        // Then piece is removed from end tile
        if((endX - startX == 1) || (endX - startX == -1)){
            tileBoard.setPiece(startX,startY,tileBoard.getPiece(endX,endY));
            tileBoard.setPiece(endX,endY,null);
        }
    }

    /**
     * Method to make a final move on the board both visually and back end
     * This method can handle any type of move for any player
     * This includes handling moving kings as well as creating new kings when needed
     * @param sX - x-coordinate of where the piece starts
     * @param sY - y-coordinate of where the piece starts
     * @param eX - x-coordinate of where the piece moves to
     * @param eY - y-coordinate of where the piece moves to
     * @param color - colour of which player's turn it is
     * @return whether or not current piece has just become a king
     */
    public int makeOfficialMove(int sX, int sY, int eX, int eY, Color color){
        // If game has already been won, then do not allow any moves
        if(gameWon() == null){
            GamePiece a = null;
            int b = 0;
            int findX = 0;
            int findY = 0;
            int capture = 0;

            // Check if this move is a capturing move and where the middle piece is if it is
            // flag that it is a capturing move in capture
            if(sX - eX == 2 && sY - eY == 2){
                findX = sX - 1;
                findY = sY - 1;
                capture = 1;
            }
            else if(sX - eX == 2 && sY - eY == -2){
                findX = sX - 1;
                findY = sY + 1;
                capture = 1;
            }
            else if(sX - eX == -2 && sY - eY == 2){
                findX = sX + 1;
                findY = sY - 1;
                capture = 1;
            }
            else if(sX - eX == -2 && sY - eY == -2){
                findX = sX + 1;
                findY = sY + 1;
                capture = 1;
            }

            for(int i = 0; i < pieces.getChildren().size(); i++){
                GamePiece g = (GamePiece) pieces.getChildren().get(i);
                if(g.getX() == sX && g.getY() == sY){
                    // Find piece that is to be moved within group
                    a = g;
                }
                if(g.getX() == findX && g.getY() == findY){
                    // Find location in group where the middle piece is
                    b = i;
                }
            }

            // Relocate used to visually move piece on board
            a.relocate(10 + 60 * eX, 10 + 60 * eY);
            // Sets x and y values of new position
            a.setX(eX);
            a.setY(eY);

            // Flag to check if a captured piece was a king
            int wasAKing = 0;

            // If this move is a capture
            if(capture == 1){
                // Completes check to see if piece was king
                if(tileBoard.getPiece(findX, findY).getKing() == true){
                    // If piece was a king, sets flag to 1
                    wasAKing = 1;
                }
                // Remove middle piece from back end
                tileBoard.setPiece(findX, findY, null);
                // Remove middle piece visually (by removing from group)
                pieces.getChildren().remove(b);
            }
            // Set piece in tile end to piece from tile start
            tileBoard.setPiece(eX, eY, tileBoard.getPiece(sX,sY));
            // If captured piece was a king, makes piece that took a king
            if(wasAKing == 1){
                tileBoard.getPiece(eX, eY).makeKing();
            }
            // Check if piece should now be a king (has reached the king's row)
            Boolean king = checkForKing(tileBoard.getTile(eX,eY), color);
            if(king == true){
                wasAKing = 1;
            }
            // Removes piece from start
            tileBoard.setPiece(sX, sY, null);

            //Returns whether or not piece has become a king
            return wasAKing;
        }
        else{
            // Output to screen statement
            System.out.println("Game already won!");
        }
        // Return -1 to indicate not make any moves
        return -1;
    }

    /**
     * Method to check whether a piece should now be a king
     * If a piece has reached the king's row at the other player's baseline
     * @param t
     * @param c
     * @return
     */
    public Boolean checkForKing(Tile t, Color c){
        // Checks that piece is not already a king
        if(t.getPiece().getKing() == false){
            // If current player has blue pieces
            if(t.getPiece().getColor() == Color.BLUE){
                if(t.getPiece().getY() == 0){
                    // If the piece reaches row 0, make piece a king
                    t.getPiece().makeKing();
                    return true;
                }
            }
            // If current player has red pieces
            else{
                if(t.getPiece().getY() == 7){
                    // If piece reaches row 7, make piece a king
                    t.getPiece().makeKing();
                    return true;
                }
            }
        }
        // Return false if not king was made
        return false;
    }

    /**
     * Method to evaluate an heuristic value from current board
     * Used at the end of paths in minimax function to generate a value
     * @param c - the current player's colour
     * @return heuristic value representing current state of board
     */
    public int evaluate(Color c){
        // Initialise value to 0;
        int value = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                // If the tile has a piece
                if(tileBoard.hasPiece(j,i) == true){
                    // If the current player is red
                    if(c == Color.RED){
                        if(tileBoard.getPiece(j,i).getKing()){
                            // Add 4 points per red king
                            if(tileBoard.getPiece(j,i).getColor() == Color.RED){
                                value = value + 4;
                            }
                            // Subtract 4 points per blue king
                            else if(tileBoard.getPiece(j,i).getColor() == Color.BLUE){
                                value = value - 4;
                            }
                        }
                        // Add 2 points per regular red piece
                        else if(tileBoard.getPiece(j,i).getColor() == Color.RED){
                            value = value + 2;
                        }
                        // Subtract 2 points per regular blue piece
                        else if(tileBoard.getPiece(j,i).getColor() == Color.BLUE){
                            value = value - 2;
                        }
                    }
                    // If current player is blue
                    else if(c == Color.BLUE){
                        if(tileBoard.getPiece(j,i).getKing()){
                            // Subtract 4 points for a red king
                            if(tileBoard.getPiece(j,i).getColor() == Color.RED){
                                value = value - 4;
                            }
                            // Add 4 points for a blue king
                            else if(tileBoard.getPiece(j,i).getColor() == Color.BLUE){
                                value = value + 4;
                            }
                        }
                        // Subtract 2 points per regular red piece
                        else if(tileBoard.getPiece(j,i).getColor() == Color.RED){
                            value = value - 2;
                        }
                        // Add 2 points per regular blue piece
                        else if(tileBoard.getPiece(j,i).getColor() == Color.BLUE){
                            value = value + 2;
                        }
                    }
                }
            }
        }
        // return heuristic evaluation of board in form of int
        return value;
    }

    /**
     * Method to setup the board and initialise all the tiles and pieces
     * This method also sets up which player is which colour
     * @param colour - String to represent which colour option chosen in main menu
     */
    public void setupBoard(String colour){
        // If option selected was "Red". Make player colour red and ai colour to blue
        if(colour == "Red"){
            playerColor = Color.RED;
            aiColor = Color.BLUE;
            turn = 0;
        }
        // If option selected was "Blue". Make player colour blue and ai colour to red
        else if(colour == "Blue"){
            playerColor = Color.BLUE;
            aiColor = Color.RED;
            turn = 1;
        }
        // If option selected was "Random", then randomly set player and ai to colours
        else{
            int num = (int) Math.random();
            if(num == 0){
                playerColor = Color.RED;
                aiColor = Color.BLUE;
                turn = 0;
            }else{
                playerColor = Color.BLUE;
                aiColor = Color.RED;
                turn = 1;
            }
        }

        // a keeps track of white or black tile
        int a = 0;
        // b keeps track of when the current row ends so that the next colour is opposite of first in row above
        int b = 0;

        // Loop through every position in the 8x8 grid
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){

                // If a is 0, make a white tile and add to grid and tileBoard
                if(a == 0){
                    Tile tile = new Tile(Color.WHITE,j,i);
                    // bug fix
                    grid.getChildren().add(tile);
                    tileBoard.setTile(j,i,tile);
                    a = 1;
                }
                // If a is 1, make a black tile and add to grid and tileBoard
                else {
                    Tile tile = new Tile(Color.BLACK,j,i);
                    grid.getChildren().add(tile);
                    tileBoard.setTile(j,i,tile);
                    a = 0;
                }
                // In the correct positions in the first three rows, add red pieces
                // Add them to the group and tileBoard
                if(((i == 0) && (j % 2 != 0)) ||
                        ((i == 1) && (j % 2 == 0)) ||
                            ((i == 2) && (j % 2 != 0))){
                    GamePiece piece = createPiece(Color.RED,j,i);
                    //board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                // Add blue pieces to group and tileBoard
                if((i == 5 || i == 7) && (j == 0 || j == 2 || j == 4 || j == 6)){
                    GamePiece piece = createPiece(Color.BLUE,j,i);
                    //board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                // Add blue pieces to group and tileBoard
                else if((i == 6) && (j == 1 || j == 3 || j == 5 || j == 7)){
                    GamePiece piece = createPiece(Color.BLUE,j,i);
                    //board.setPiece(piece,j,i);
                    tileBoard.setPiece(j,i,piece);
                    pieces.getChildren().add(piece);
                }
                b++;
                // Used to switch back and forth between black and white on each row
                // Also used to switch colours when entering knew column
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

    /**
     * Method to use minimax and act as a successor function to identify possible moves
     * Method also uses these functions to find the optimal move
     * This method then calls another method to display on the screen this help to the player
     * @param d - the chosen depth that both the ai and player minimax calls can go
     */
    public void playerHint(int d){
        // Creates an ArrayList to store all the possible moves
        ArrayList<Tile> possMoves = new ArrayList<>();
        Tile bestMove = null;
        flag = 0;

        // Initialise bestVal to very small number
        int bestVal = -10000000;
        // Sets alpha/beta to negative and positive infinity respectively
        int alpha = (int) Double.NEGATIVE_INFINITY;
        int beta = (int) Double.POSITIVE_INFINITY;


        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                // Loop through board and find tiles that have a piece
                if(tileBoard.getPiece(j,i) != null){
                    // If piece belongs to player
                    if(tileBoard.getPiece(j,i).getColor() == playerColor){
                        // Get all the possible moves from this piece
                        ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), playerColor);
                        for(int y = 0; y < posMoves.size(); y++){
                            // Add each possible move to the ArrayList of possMoves
                            possMoves.add(posMoves.get(y));
                        }
                        // check if a capture has been found, if not check for a capture
                        if(flag == 0){
                            for(int k = 0; k < posMoves.size(); k++){
                                if(posMoves.get(k).returnX() - j == 2 || posMoves.get(k).returnX() - j == -2){
                                    // set flag to capture to 1
                                    // set bestMove to this move
                                    flag = 1;
                                    bestMove = posMoves.get(k);
                                }
                            }
                        }
                        for(int k = 0; k < posMoves.size(); k++){
                            // Move to piece, check for a king and run minimax to find value similar to runAiMove
                            makeMove(tileBoard.getTile(j,i), posMoves.get(k), playerColor);
                            Boolean king = checkForKing(posMoves.get(k), playerColor);
                            // Call minimax to get heuristic value of this move
                            int test = minimax(posMoves.get(k),d,alpha,beta, playerColor, aiColor, "MAX");
                            // Unmake move and readjust king if needed
                            unmakeMove(tileBoard.getTile(j,i), posMoves.get(k), playerColor);
                            if(king == true){
                                tileBoard.getTile(j,i).getPiece().removeKing();
                            }
                            // value is better than current value
                            if(test > bestVal){
                                if(flag == 1){
                                    // if flag is true, only change position to another better capture
                                    if(posMoves.get(k).returnX() - j == 2 || posMoves.get(k).returnX() - j == -2){
                                        // update bestMove
                                        bestMove = posMoves.get(k);
                                    }
                                    // if flag is false, set to better option regardless of if it is a capture or not
                                }else{
                                    // if not capturing moves found but better value than previous ones
                                    // update bestMove
                                    bestVal = test;
                                    bestMove = posMoves.get(k);
                                }

                            }
                        }
                    }
                }
            }
        }
        // Sets all possible moves found to hintPossMoves
        hintPossMoves = possMoves;
        // Sets best move found to hintBestMove
        hintBestMove = bestMove;
    }

    /**
     * Method to update tiles to visually show hints
     * Possible moves updated to have an orange stroke
     * Best move updated to have a larger gold stroke
     */
    public void showHints(){
        for(int i = 0; i < hintPossMoves.size(); i++){
            // Add orange stroke to this tile
            hintPossMoves.get(i).showAsPossibleMove();
        }
        // Add gold stroke to this position
        hintBestMove.showAsBestPossibleMove();
    }

    /**
     * Removes all strokes from the tiles
     * This is for both possible tiles and best tile
     */
    public void hideHints(){
        for(int i = 0; i < hintPossMoves.size(); i++){
            // Removes orange stroke
            hintPossMoves.get(i).unshowAsPossibleMove();
        }
        // Remove gold stroke
        hintBestMove.unshowAsPossibleMove();
    }

    /**
     * Method to create a piece
     * Used to add a on click listener that has access to all main functions
     * @param color - colour of the player this piece belongs to
     * @param x - x-coordinate of piece on grid
     * @param y - y-coordinate of piece on grid
     * @return piece is made and then returned
     */
    public GamePiece createPiece(Color color, int x, int y){
        // Creates a new piece
        GamePiece piece = new GamePiece(color,x,y);

        // On release handler to manage player interaction with pieces
        piece.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Check that it players turn
                if(turn == 0){
                    // Reset text output
                    commandOutput.setText("");

                    // get current position in the board
                    int oldX = piece.getX();
                    int oldY = piece.getY();

                    // get new position based on where the piece was released
                    // make an int so that it can be divided nicely by size of tile
                    int newX = (int) mouseEvent.getSceneX();
                    int newY = (int) mouseEvent.getSceneY();
                    // Divide by size of tile to get a coordinate position in grid
                    newX = newX / 60;
                    newY = newY / 60;

                    // Check if the player must capture a piece or not
                    Boolean forceCapture = mustCapture(playerColor);
                    // checks that it is players turn, otherwise output on screen that it is not their turn
                    if(piece.getColor() == playerColor){
                        // check location clicked is a valid place on the board
                        if(newX <= 7 && newX >= 0 && newY >= 0 && newX <= 7){
                            // if capture is needed
                            if(forceCapture == true){
                                // check that move selected was a capture
                                if((newX - oldX == 2 || newX - oldX == -2)){
                                    // make sure it is a valid move
                                    if(validMove(oldX, oldY, newX, newY) == true){
                                        // make move
                                        makeOfficialMove(oldX, oldY, newX, newY, playerColor);
                                        // check for a multi-leg capture
                                        if(checkDoubleTake(tileBoard.getTile(newX, newY),playerColor) == null){
                                            // if player takes piece but no second piece available
                                            // remove hints
                                            if(hintActive == true){
                                                hideHints();
                                            }
                                            // end turn
                                            turn = 1;
                                        }
                                    }
                                }
                                // Not a capture chosen
                                else{
                                    // Visually output that a capture is available and required
                                    commandOutput.setText("Must capture is available");
                                }
                            }
                            // No capture needed
                            else{
                                // If location is a valid move (diagonal and moving by 1 forward (or backwards if king)
                                if(validMove(oldX, oldY, newX, newY) == true){
                                    // make move
                                    makeOfficialMove(oldX, oldY, newX, newY, playerColor);
                                    // close the hints if shown
                                    if(hintActive == true){
                                        hideHints();
                                    }
                                    // end turn
                                    turn = 1;
                                }
                                // Not a valid place on board
                                else{
                                    // Output visually command that this was not a valid move
                                    commandOutput.setText("Invalid move");
                                }
                            }
                        }
                    }
                }
                // Not player's turn
                else{
                    // Visually output that it is ai's turn
                    commandOutput.setText("It is ai's turn");
                }
            }
        });
        // returns piece
        return piece;
    }

    /**
     * Method to check if a player must make a capture on next turn
     * @param c - colour of player
     * @return true if they must make a capture, false otherwise
     */
    public Boolean mustCapture(Color c){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                // Loop through all the tiles and find pieces
                if(tileBoard.hasPiece(j,i) == true){
                    // If the piece is of the player's colour
                    if(tileBoard.getPiece(j,i).getColor() == c){
                        ArrayList<Tile> posMoves = possibleMoves(tileBoard.getTile(j,i), c);
                        for(int k = 0; k < posMoves.size(); k++){
                            // Check if move is a capture
                            if((posMoves.get(k).returnX() - j == 2 || posMoves.get(k).returnX() - j == -2)){
                                // If move is a capture, return true
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // otherwise return false
        return false;
    }

    /**
     * Method that checks if a move is valid
     * Uses possibleMoves to achieve this
     * @param oldX - x-coordinate of old tile
     * @param oldY - y-coordinate of old tile
     * @param newX - x-coordinate of new tile
     * @param newY - y-coordinate of new tile
     * @return true if move is valid, false otherwise
     */
    public Boolean validMove(int oldX, int oldY, int newX, int newY){
        // Get an ArrayList of all possible moves from old position
        ArrayList<Tile> validMoves = possibleMoves(tileBoard.getTile(oldX,oldY),playerColor);
        for(int i = 0; i < validMoves.size(); i++){
            if(validMoves.get(i) == tileBoard.getTile(newX, newY)){
                // If one of those moves is equal to new tile, return true;
                return true;
            }
        }
        return false;
    }

    /**
     * Method to create the game scene
     * @param difficulty - the chosen difficulty as a string
     * @param colour - the chosen colour setting as a string
     * @return game scene to be displayed in stage
     */
    public Scene createScene(String difficulty, String colour, Stage stage){
        // Creates a new pane to hold tiles
        Pane scene = new Pane();
        // Sets to size of 8x8 grid of 60x60 tiles
        scene.setPrefSize(480,480);
        // Adds all tiles and pieces groups to pane
        scene.getChildren().addAll(grid, pieces);
        // Calls setup board to create all the tiles and pieces to put into the groups
        setupBoard(colour);

        Text playerColorLabel = new Text();
        if(playerColor == Color.RED){
            playerColorLabel.setText("Player Colour: Red");
        }
        else{
            playerColorLabel.setText("Player Colour: Blue");
        }
        // Create an end turn button which will end player's turn and start ai's turn
        Button endTurn = new Button("End Turn");
        // On click handler to run ai turn when clicked
        endTurn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(turn == 1){
                    // Integer to represent level of difficulty
                    int d = 0;
                    if(difficulty == "Easy"){
                        d = 1;
                    } else if(difficulty == "Medium"){
                        d = 3;
                    } else{
                        d = 9;
                    }
                    try {
                        runAITurn(d);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // End turn after ai makes a move
                    turn = 0;
                }
                // Not ai's turn
                else{
                    // Visually output that it is not ai's turn
                    commandOutput.setText("It is your turn");
                }
            }
        });

        // Help button to use minimax to suggest moves including a best move
        Button help = new Button("Help");
        // On click handler to change visuals to show help
        help.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int d = 0;
                if(difficulty == "Easy"){
                    d = 1;
                } else if(difficulty == "Medium"){
                    d = 3;
                } else{
                    d = 9;
                }
                // If hints are off, generate hints and show them
                if(hintActive == false){
                    playerHint(d);
                    showHints();
                    hintActive = true;
                }
                // If hints are already on, turn them off
                else{
                    hideHints();
                    hintActive = false;
                }
            }
        });

        // Pop-up box for rules of the game
        Button rules = new Button("Rules");
        // On click handler to manage event of clicking the button
        rules.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Creates a new pop-up box and sets height and width
                Popup rulesPopup = new Popup();
                rulesPopup.setWidth(400);
                rulesPopup.setHeight(600);
                // Creates a title for pop-up menu
                Text titleOfPopup = new Text("Rules of the game:");
                // Sets size of title to font size 24
                titleOfPopup.setFont(Font.font(24));
                // Adds a gold stroke and underline to make title stand out
                titleOfPopup.setStroke(Color.GOLD);
                titleOfPopup.setUnderline(true);
                // A text is created to hold all the rules of the game
                Text allRules = new Text("1: Blue always goes first\n2: Each player has 12 pieces" +
                        "\n3: Regular pieces can only move forwards diagonally\n4: Kings can move any direction diagonally" +
                        "\n5: A piece becomes a king once it reaches the opposite end of the grid\n6: Pieces can only move 1 space forwards unless completing a capture" +
                        "\n7: Both players must complete a capture if one is available, but can pick which capture to take if multiple are available" +
                        "\n8: Player can capture multiple pieces in one go if they are available by that same piece in continuous move" +
                        "\n9: If a player takes a piece and reaches the end to become a king, it cannot capture another piece even if they are available" +
                        "\n10: The game is won when all of the opponents pieces have been captured");
                // Sets size of rules to font size 16
                allRules.setFont(Font.font(16));
                // Created a button to close rules pop-up
                Button closeRules = new Button("Close Rules");
                // Added on click handler to close pop-up screen once clicked
                closeRules.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        rulesPopup.hide();
                    }
                });
                // Used a VBox with spacing and CSS styling to create a visually suitable pop-up
                VBox items = new VBox(titleOfPopup,allRules,closeRules);
                items.setSpacing(20);
                // CSS styling of rules
                items.setStyle(" -fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-alignment: center; ");
                rulesPopup.getContent().addAll(items);
                // Displays pop-up to screen
                rulesPopup.show(stage);
            }
        });

        // TextField to use for display messages to player
        commandOutput = new TextField("Output:");

        // Create the scene
        VBox buttons = new VBox(playerColorLabel,endTurn,help, rules);
        buttons.setSpacing(20);
        buttons.setStyle("-fx-padding: 10; -fx-alignment: center; ");
        HBox boardAndButtons = new HBox(scene, buttons);
        VBox completeScene = new VBox(boardAndButtons, commandOutput);
        // CSS styling of scene
        completeScene.setStyle(" -fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-alignment: center; ");
        Scene gameScene = new Scene(completeScene);
        return gameScene;
    }

    /**
     * Default main
     * @param args
     */
    public static void main(String args[]){
        launch(args);
    }
}
