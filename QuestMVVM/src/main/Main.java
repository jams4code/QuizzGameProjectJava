package main;

import Mediator.MediatorViewModel;
import static Mediator.MediatorViewModel.getInstance;
import viewmodel.ViewModelFederation;
import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import view.ViewFederation;

/**
 * Main classe.
 * @author Jamal&MehdiEPFC
 */
public class Main extends Application {

    /**
     * The main method that launch the application.
     * @param args Command-Line arguments passed to the Java program upon invocation.
     */
    public static void main(String[] args) {
         launch(args);
    }
    /**
     * Start method is creating the instances of the objects and views that are necessary to launch the application
     * @param primaryStage The first stage that can be seen when the application will be launched.
     * @throws Exception If there is an error/exception in any method/submethod this method will throw an exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Federation federation = new Federation();
        MediatorViewModel med = getInstance();
        ViewModelFederation viewModelfd = new ViewModelFederation(federation);
        ViewFederation view = new ViewFederation(primaryStage, viewModelfd);
        med.addViewModel(viewModelfd);
        primaryStage.show();
    }
}
