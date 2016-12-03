package minesweeper.view;/**
 * Created by Caevol on 12/1/2016.
 */

import minesweeper.controller.GridSquare;
import minesweeper.controller.GameGrid;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * main class that builds UI and runs it
 * also contains main function
 * @author Logan Pedersen
 * @version 1.0
 */
public class GUIDisplay extends Application {

    /**
     * main function
     * @param args command line arguments, have no impact.
     */
    public static void main(String[] args) {
        launch(args);
    }

    public GameGrid game;
    public BorderPane mainPane;
    public Toolbar toolbar;


    /**
     * Puts the main UI elements together and gives buttons their effects
     * @param primaryStage Stage used for application
     */
    @Override
    public void start(Stage primaryStage) {
        toolbar = new Toolbar();
        game = new GameGrid();
        mainPane = new BorderPane();
        mainPane.setCenter(game);
        mainPane.setTop(toolbar);


        game.setClickEventForButtons(this::clickAction);
        toolbar.setStartGameAction(e -> {game.startGame(); toolbar.startGame.setDisable(true);});
        toolbar.bombs.textProperty().bind(Bindings.convert(game.bombProperty));
        toolbar.time.textProperty().bind(Bindings.convert(game.timeProperty));
        Scene mainScene = new Scene(mainPane);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

            @Override
            public void handle(WindowEvent event) {
                game.animationTimer.stop();
            }
        });


    }

    /**
     * ActionEvent used for all buttons, checks for right or left click and behaves like standard minesweeper
     * @param actionEvent mouse click event
     */
    private void clickAction(MouseEvent actionEvent) {
        if(actionEvent.getButton() == MouseButton.SECONDARY){
            GridSquare g = (GridSquare)actionEvent.getSource();
            if(!(g.isDisabled()))
            {
                if(g.isMarked)
                {
                    if(g.getId().equals("BombMark"))
                    {
                        g.questionMarkSquare();

                    }
                    else
                    {
                        g.unmarkSquare();
                        game.bombProperty.setValue(game.bombProperty.getValue() +1);
                    }

                }
                else
                {


                    g.markSquare();
                    game.bombProperty.setValue(game.bombProperty.getValue() -1);
                }
            }

        }
        else
        {
            if(game.gameOver == true)
                game.gameOver = false;

            if(((GridSquare)actionEvent.getSource()).isDisabled() != true && ((GridSquare)actionEvent.getSource()).isMarked != true) {
                clickOnMine(actionEvent);

            }
        }



    }

    /**
     * Open the square and view its contents, ends game if a mine, else it progresses the game and displays boxes number
     * of mined neighbors
     * @param actionEvent mouse event
     */
    private void clickOnMine(MouseEvent actionEvent){
        //this spot is a mine


        if (((GridSquare)actionEvent.getSource()).openSquare()){
            loseGame();
        }
        //the spot is not a mine
        else
        {

                game.unopenedSpaces--;
                int index = ((GridSquare) actionEvent.getSource()).posX + (((GridSquare) actionEvent.getSource()).posY * 20);

                if (((GridSquare) actionEvent.getSource()).getAdjacentMines() == 0)
                    game.unopenedSpaces -= game.openAdjacentSquares(index);

                if (game.checkGameCompleted()) {
                    winGame();
                }

        }
    }

    /**
     * basic win game, gives a dialogue box and displays mine positions
     */
    private void winGame(){
        game.gameOver = true;
        game.winUpdateSquares();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You cleared the field with a time of " + game.timeProperty.get() + " seconds!");
        alert.setHeaderText(null);
        alert.showAndWait();

        toolbar.startGame.setDisable(false);

    }

    /**
     * lose game events, gives a dialogue box, displays mine positions.
     */
    private void loseGame(){
        game.gameOver = true;
        game.gameOverUpdateSquares();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("You lost in " + game.timeProperty.get() + " seconds");
        alert.showAndWait();
        toolbar.startGame.setDisable(false);
    }


}
