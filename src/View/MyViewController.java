package View;

import ViewModel.ViewModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class MyViewController implements IView {

    @FXML
    private Canvas canvas;
    private ViewModel viewModel;


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void PrintRectangle(int i, int j) {

    }
}
