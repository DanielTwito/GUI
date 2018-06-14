package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.GridPane;

public class CharacterDisplayer extends GridPane {

    private double width;
    private double height;
    private int columns;
    private int rows;
    private double cellWidth;
    private double cellHeight;

    private StringProperty imageFilePath = new SimpleStringProperty();

    public String getImageFilePath() {
        return imageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.set(imageFilePath);
    }

    public CharacterDisplayer(double width, double height, int columns, int rows) {
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.rows = rows;
        cellWidth = width / (double) columns;
        cellHeight = height / (double) rows;
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

    public void draw(int rowIndex, int columnIndex) {
        getChildren().clear();
        Cell cell = new Cell(cellWidth, cellHeight, imageFilePath.toString());
        add(cell, columnIndex, rowIndex);
        cell.draw();
    }
}
