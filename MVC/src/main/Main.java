package main;

import ctrl.Ctrl;
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
        Ctrl ctrl = new Ctrl(federation);
        View view = new View(primaryStage, ctrl);
        federation.addObserver(view);
        federation.notif(Notif.INIT);
        primaryStage.show();
    }
}
