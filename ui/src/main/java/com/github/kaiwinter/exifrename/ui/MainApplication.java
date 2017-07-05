package com.github.kaiwinter.exifrename.ui;

import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class.getSimpleName());

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("exif rename");

        ViewTuple<MainView, MainViewModel> viewTuple = FluentViewLoader.fxmlView(MainView.class).load();

        Parent root = viewTuple.getView();
        stage.setScene(new Scene(root));
        stage.show();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> Platform.runLater(() -> {
            LOGGER.error(throwable.getMessage(), throwable);
            ExceptionDialog exceptionDialog = new ExceptionDialog(throwable);
            exceptionDialog.show();
        }));
    }

}
