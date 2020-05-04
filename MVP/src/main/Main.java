package main;

import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import presenter.Presenter;
import view.ViewImplements;

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
        Presenter presenter = new Presenter(federation);
        ViewImplements view = new ViewImplements(primaryStage);
        view.setPresenter(presenter);
        presenter.setView(view);
        primaryStage.show();
    }
}
