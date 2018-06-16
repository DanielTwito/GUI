package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private IModel model;
    private ArrayList<Position> loc;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private boolean isReached=false;

    public StringProperty characterPositionRow = new SimpleStringProperty(); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty(); //For Binding

    /**
     * c'tor
     * @param model
     */
    public ViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            isReached=model.isReached();
            setChanged();
            notifyObservers(arg);
        }
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public ArrayList<Position> getSolution() {
        loc = model.getSolutionPath();
        return loc;
    }

    public void generateMaze(int width, int height){
        loc = null;
        isReached=false;
        model.generateMaze(height,width);
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public boolean isReached() {
        return isReached;
    }

    public void loadMaze(File file) {
        model.loadMaze(file);
    }

    public void saveMaze(File file){
        model.saveMaze(file);
    }


}
