package View;

import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SolutionDisplayer extends Displayer {

        private ArrayList<Position> sol;

    public void setList(ArrayList<Position> solList) {

        this.sol = solList;
        redraw();
    }

    public void clearSolution() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }


    public void redraw() {
        if (sol != null) {
            try {
                Image solImage = new Image(new FileInputStream(ImageFileNameSol.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Sol
                for (Position cord : sol) {
                    gc.drawImage(solImage, cord.getColumnIndex() * getCellWidth(), cord.getRowIndex() * getCellHeight(), getCellWidth(), getCellHeight());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private StringProperty ImageFileNameSol = new SimpleStringProperty();

    public String getImageFileNameSol() {
        return ImageFileNameSol.get();
    }

    public void setImageFileNameSol(String imageFileNameSol) {
        this.ImageFileNameSol.set(imageFileNameSol);
    }
}
