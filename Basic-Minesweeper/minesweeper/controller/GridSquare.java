package minesweeper.controller;

import javafx.scene.control.Button;

/**
 * A single gridsquare that inherits from button
 * Keeps track of location, adjacent mine count, and if it is a mine
 * Created by Caevol on 12/1/2016.
 */
 public class GridSquare extends Button {

    public boolean isMarked;
    private boolean isMine;
    private int adjacentMines;
    public int posX, posY;


    /**
     * constructor, uses css/custom.css for styling
     * size should be kept at 25 to prevent odd behaviors
     */
    public GridSquare(){
        this.getStylesheets().addAll("minesweeper/css/custom.css");
        isMarked = false;
        this.posX = 0;
        this.posY = 0;
        adjacentMines = 0;
        isMine = false;

        setMinWidth(25);
        setMaxHeight(25);
    }

    /**
     * tells the mine its current location, is not used directly for anything but counting neighbors
     * @param posX position x
     * @param posY position y
     */
    public void setNewLocation(int posX, int posY){
        this.posX = posX;
        this.posY = posY;

    }

    /**
     * tells the square it is marked with a flag
     */
    public void markSquare(){
        isMarked = true;
        this.setId("BombMark");
        //this.setText("x");
    }

    /**
     * tells the square it is marked with a question mark
     */
    public void questionMarkSquare(){
        this.setId("QuestionMark");
        //this.setText("?");
    }

    /**
     * removes markings and allows button to be pressed again
     */
    public void unmarkSquare(){
        isMarked = false;
        this.setText("");
        this.setId("");
    }

    /**
     * basic setter
     * @param mineCount sets adjacentMines
     */
    public void setAdjacentMines(int mineCount){
        adjacentMines = mineCount;
    }

    /**
     * basic getter
     * @return adjacent mine count
     */
    public int getAdjacentMines(){
        return adjacentMines;
    }

    /**
     * gets whether or not the square is a mine
     * @return isMine
     */
    public boolean isMine(){
        return isMine;
    }


    /**
     * sets the square as a mine or not
     * @param isMine whether or not it is a mine
     */
    public void setMine(boolean isMine){
        this.isMine = isMine;
    }

    /**
     * clears the square of all details and resets position. For game reset.
     */
    public void resetSquare(){
        isMarked = false;
        posX = 0;
        posY = 0;
        adjacentMines = 0;
        isMine = false;
        this.setDisable(false);
        this.setText("");
        this.setId("");
    }

    /**
     * opens the square and sets the id
     * @return True if a mine, false if not
     */
    public boolean openSquare() {
        this.setDisabled(true);

        if(!isMine){
            this.setText("" + adjacentMines);


        }
        else{
            this.setId("Mined");
        }
        return isMine;

    }





}
