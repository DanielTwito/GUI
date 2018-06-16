package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CharacterDisplayer extends Displayer {

    private int characterPositionRow ;
    private int characterPositionColumn;

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
    private StringProperty imageFileNameGoal = new SimpleStringProperty();


    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }


    public void redraw() {

        try {
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            //Draw Character
            gc.drawImage(characterImage, characterPositionColumn * getCellWidth(), characterPositionRow * getCellHeight(), getCellWidth(), getCellHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
}
