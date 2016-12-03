package minesweeper.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Basic HBox at the top
 * contains a label for bombs, time and a start button
 * Created by Caevol on 12/1/2016.
 */
public class Toolbar extends HBox {

    public Label bomb1;
    public Label bombs;

    public Label time1;
    public Label time;
    public Button startGame;


    /**
     * constructor, builds all menu items and adds them in order
     */
    public Toolbar(){
        bomb1 = new Label("Bombs: ");
        bombs = new Label("0");
        bombs.setPadding(new Insets(30, 30, 30, 5));
        time1 = new Label("Time: ");
        time1.setPadding(new Insets(30, 5, 30, 30));
        time = new Label("0");
        time.setPadding(new Insets(30, 30, 30, 5));
        startGame = new Button("Start Game");
        this.getChildren().addAll(bomb1, bombs, startGame, time1, time);

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));
    }

    /**
     * sets the start game button
     * @param e actionEvent
     */
    public void setStartGameAction(EventHandler<ActionEvent> e)
    {
        startGame.setOnAction(e);
    }


}
