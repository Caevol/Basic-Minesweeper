package minesweeper.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Grid that contains all GridSquares, runs most logic and keeps track of win and lose conditions.
 * NUMBER_OF_BOMBS can be changed safely to any number between 1 and 400
 * Created by Caevol on 12/1/2016.
 */
public class GameGrid extends GridPane {

    private final int NUMBER_OF_BOMBS = 100; //this one works, change it to change number of bombs
    private final int NUMBER_OF_CELLS = 400; //changes number of cells spawned, broken if not set to 400
    public AnimationTimer animationTimer;
    public boolean gameOver;
    public int unopenedSpaces;
    public ArrayList<GridSquare> mineGrid;
    public SimpleIntegerProperty bombProperty;
    public SimpleIntegerProperty timeProperty;
    private long timeStamp, tmp, diff;


    /**
     * constructor that builds the board, places every cell, does not place mines until game starts.
     */
    public GameGrid() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                diff = now - tmp;
                tmp = now;
                if(now - timeStamp > 1000000000){
                    timeStamp = now;
                    if(gameOver == false)
                    timeProperty.setValue(timeProperty.getValue() + 1); //increment time once
                }
            }
        };

        this.setPadding(new Insets(10, 0, 0, 10));
        this.setWidth(500);
        this.setHeight(500);

        unopenedSpaces = NUMBER_OF_CELLS - NUMBER_OF_BOMBS;
        bombProperty = new SimpleIntegerProperty(this, "bombProperty");
        bombProperty.set(NUMBER_OF_BOMBS);
        timeProperty = new SimpleIntegerProperty(this, "timeProperty");
        timeProperty.set(0);
        gameOver = true;
        animationTimer.start();
        //construct minegrid
        mineGrid = new ArrayList<GridSquare>();

        for (int j = 0; j < NUMBER_OF_CELLS; j++) {
            mineGrid.add(new GridSquare());
        }


        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            GridPane.setConstraints(mineGrid.get(i), i % 20, i / 20);
            GridPane.setColumnSpan(this, 30);
            GridPane.setRowSpan(this, 20);
            this.getChildren().add(mineGrid.get(i));
        }

        for(GridSquare g : mineGrid){
            g.setDisable(true);
        }
    }




    /**
     * shuffles the already mined board and sets each gridSquares position
     */
    private void setAllPositions() {
        Collections.shuffle(mineGrid);

        for (int i = 0; i < NUMBER_OF_CELLS; ++i) {
            mineGrid.get(i).setNewLocation(i % 20, i / 20);
        }
    }

    /**
     * adds NUMBER_OF_BOMBS bombs to grid
     */
    private void addMines() {
        for (int i = 0; i < NUMBER_OF_BOMBS; i++) {
            mineGrid.get(i).setMine(true);
            mineGrid.get(i).setId("Mined");
        }
    }

    /**
     * sets all squares back to default values, removes all bombs and styling.
     */
    private void resetSquares() {
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            mineGrid.get(i).resetSquare();
        }
    }

    /**
     * determine each cells number of neighbors and assigns that cell its value
     */
    private void setMineCounts() {
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            mineGrid.get(i).setAdjacentMines(countGridMines(i));

        }
    }

    /**
     * counts nearby neighbors for cell at INDEX
     * @param index cell value in mineGrid
     * @return number of neighbors that are mines
     */
    private int countGridMines(int index) {
        int count = 0;

        if (index - 20 - 1 >= 0 && index % 20 != 0) {
            if (mineGrid.get(index - 20 - 1).isMine())
                ++count;
        }
        if (index - 20 >= 0) {
            if (mineGrid.get(index - 20).isMine())
                ++count;
        }
        if (index - 20 + 1 >= 0 && index % 20 != 19) {
            if (mineGrid.get(index - 20 + 1).isMine())
                ++count;
        }
        if (index - 1 >= 0 && index % 20 != 0) {
            if (mineGrid.get(index - 1).isMine())
                ++count;
        }
        if (index + 1 < 400 && index % 20 != 19) {
            if (mineGrid.get(index + 1).isMine())
                ++count;
        }
        if (index + 20 - 1 < 400 && index % 20 != 0) {
            if (mineGrid.get(index + 20 - 1).isMine())
                ++count;
        }
        if (index + 20 < 400) {
            if (mineGrid.get(index + 20).isMine())
                ++count;
        }
        if (index + 20 + 1 < 400 && index % 20 != 19) {
            if (mineGrid.get(index + 20 + 1).isMine())
                ++count;
        }

        return count;
    }

    /**
     * places bombs, sets win conditions and allows the game to be played
     */
    public void startGame() {

        resetSquares();
        //gameOver = false;
        unopenedSpaces = NUMBER_OF_CELLS - NUMBER_OF_BOMBS;
        bombProperty.set(NUMBER_OF_BOMBS);
        timeProperty.set(0);
        addMines();
        setAllPositions();
        setMineCounts();
        this.getChildren().clear();

        for (int i = 0; i < NUMBER_OF_CELLS; ++i) {
            mineGrid.get(i).setDisable(false);
            GridPane.setConstraints(mineGrid.get(i), i % 20, i / 20);
            this.getChildren().add(mineGrid.get(i));
        }


    }

    /**
     * Lose condition freeze board, displays mistakes and bombs
     */
    public void gameOverUpdateSquares(){
        for(GridSquare g : mineGrid){
            if(g.isMarked){
                if(g.isMine()){
                    g.setId("SafeBomb");
                }
                else
                    g.setId("WrongMarkedSquare");


            }
            else{
                if(g.isMine()){
                    g.setId("Mined");
                }
                else{
                    if(!g.isDisabled())
                    {
                        g.setId("UnopenedSquare");
                    }
                }
            }

           // g.setText("");
            g.setDisable(true);
        }
    }

    /**
     * win condition board freeze, displays bombs in green and freezes board
     */
    public void winUpdateSquares(){
        for(GridSquare g : mineGrid){

                if(g.isMine()){
                    g.setId("SafeBomb");
                }

            //g.setText("");
            g.setDisable(true);
        }
    }

    /**
     * checks to see if game has been won
     * @return true if game has been won
     */
    public boolean checkGameCompleted() {
        if (unopenedSpaces <= 0) {
            gameOver = true;
        }
        return gameOver;
    }

    /**
     * sets action event for all grid squares,
     * @param e behavior for grid squares
     */
    public void setClickEventForButtons(EventHandler<MouseEvent> e) {
        for (GridSquare g : mineGrid) {
            g.setOnMouseClicked(e);
        }
    }

    /**
     * If first cell opened had 0 mined neighbors, opens all neighbors and runs recursively on all 0 value squares
     * @param index cell position to be opened
     * @return number of cells that were opened, progresses game towards victory condition
     */
    public int openAdjacentSquares(int index){
        int count = 0;
        if (index - 20 - 1 >= 0 && index % 20 != 0) {
            if (mineGrid.get(index - 20 -1).isDisabled() != true)
            {
                mineGrid.get(index - 20 -1).openSquare();
                 count ++;

                if(mineGrid.get(index-20-1).getAdjacentMines() == 0)
                count += openAdjacentSquares(index - 20 -1);
            }

        }
        if (index - 20 >= 0) {
            if (mineGrid.get(index - 20 ).isDisabled() != true)
            {
                mineGrid.get(index - 20).openSquare();
                 count ++;

                if(mineGrid.get(index-20).getAdjacentMines() == 0)
                count += openAdjacentSquares(index - 20);
            }
        }
        if (index - 20 + 1 >= 0 && index % 20 != 19) {
            if ( mineGrid.get(index - 20 +1).isDisabled() != true)
            {
                mineGrid.get(index - 20 +1).openSquare();
                count ++ ;
                if(mineGrid.get(index-20+1).getAdjacentMines() == 0)
                count += openAdjacentSquares(index - 20 +1);
            }
        }
        if (index - 1 >= 0 && index % 20 != 0) {
            if (mineGrid.get(index -1).isDisabled() != true)
            {
                mineGrid.get(index - 1).openSquare();
                count ++ ;
                if(mineGrid.get(index-1).getAdjacentMines() == 0)
                count += openAdjacentSquares(index - 1);
            }
        }
        if (index + 1 < 400 && index % 20 != 19) {
            if ( mineGrid.get(index + 1).isDisabled() != true)
            {
                mineGrid.get(index +1).openSquare();
                count ++ ;
                if(mineGrid.get(index +1).getAdjacentMines() == 0)
                count += openAdjacentSquares(index +1);
            }
        }
        if (index + 20 - 1 < 400 && index % 20 != 0) {
            if (! (mineGrid.get(index + 20 -1).isDisabled()))
            {
                mineGrid.get(index + 20 -1).openSquare();
                count ++ ;
                if(mineGrid.get(index+20-1).getAdjacentMines() == 0)
                count += openAdjacentSquares(index + 20 -1);
            }
        }
        if (index + 20 < 400) {
            if (mineGrid.get(index + 20).isDisabled() != true)
            {
                mineGrid.get(index + 20 ).openSquare();
                count ++ ;
                if(mineGrid.get(index+20).getAdjacentMines() == 0)
                count += openAdjacentSquares(index + 20 );
            }
        }
        if (index + 20 + 1 < 400 && index % 20 != 19) {
            if ( mineGrid.get(index + 20 +1).isDisabled() != true)
            {
                mineGrid.get(index + 20 +1).openSquare();
                count ++ ;
                if(mineGrid.get(index+20+1).getAdjacentMines() == 0)
                count += openAdjacentSquares(index + 20 +1);
            }
        }

        return count;
    }


}


