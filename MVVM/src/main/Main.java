package main;

import viewmodel.ViewModel;
import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import view.View;

/**
 * Classe principal
 * @author Jamal&MehdiEPFC
 */
public class Main extends Application {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
         launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Federation federation = new Federation();
        ViewModel viewModel = new ViewModel(federation);
        View view = new View(primaryStage, viewModel);
        primaryStage.show();
    }
}
