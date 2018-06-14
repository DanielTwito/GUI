package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.GridPane;

public class SolutionDisplayer extends GridPane {
    private double width;
    private double height;
    private int columns;
    private int rows;
    //solution object*/

    private StringProperty imageFilePath = new SimpleStringProperty();

    public String getImageFilePath() {
        return imageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.set(imageFilePath);
    }


    public SolutionDisplayer(/*solution object*/double width, double height, int columns, int rows) {
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.rows = rows;
        initialize(rows, columns);
    }

    private void initialize(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            addRow(i);
        }

        for (int j = 0; j < columns; j++) {
            addColumn(j);
        }
    }

    public void draw() {
//        if (Solution != null) {
        double cellHeight = height / rows;
        double cellWidth = width / columns;

        try {
            //Draw Maze
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    if (true /*Check if solution has this index*/) {
                        Cell cell = new Cell(cellWidth, cellHeight, imageFilePath.toString());
                        add(cell, row, column);
                        cell.draw();
                    }
                }
            }
        } catch (Exception e) {
        }
        //}
    }
}
