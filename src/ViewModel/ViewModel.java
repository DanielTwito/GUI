package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("0"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("0"); //For Binding

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
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int height, int width){
        model.generateMaze(height,width);
        characterPositionRowIndex=model.getMaze().getStartPosition().getRowIndex();
        characterPositionRow.setValue(characterPositionRowIndex+"");
        characterPositionColumnIndex=model.getMaze().getStartPosition().getColumnIndex();
        characterPositionColumn.setValue(+characterPositionColumnIndex+"");
    }

    public void solveMaze(){
        model.solveMaze(getMaze());
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

    public Solution getSolution() {
        return model.getMazeSolution();
    }
}
