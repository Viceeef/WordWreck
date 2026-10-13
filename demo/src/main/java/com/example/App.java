package com.example;

import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class App extends Application {
    public static final double WIDTH = 1180, HEIGHT = 780;
    private static Stage window;
    private static StackPane viewport;
    private static Region page;
    private static boolean expanded;
    private static double oldX, oldY, oldW, oldH;

    @Override public void start(Stage stage) throws IOException {
        if (javafx.scene.text.Font.loadFont(App.class.getResourceAsStream("fonts/wreck-pixel.ttf"), 12) == null)
            throw new IOException("Missing bundled pixel font: fonts/wreck-pixel.ttf");
        window = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        // Best effort: an OS window manager can still override application requests.
        stage.iconifiedProperty().addListener((o, oldValue, minimized) -> {
            if (minimized) javafx.application.Platform.runLater(() -> {
                if (stage.isShowing()) stage.setIconified(false);
            });
        });
        viewport = new StackPane();
        viewport.setStyle("-fx-background-color: #10162c;");
        setRoot("primary");
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        double fit = Math.min(1, Math.min(screen.getWidth()/WIDTH, screen.getHeight()/HEIGHT));
        Scene scene = new Scene(viewport, WIDTH*fit, HEIGHT*fit);
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("wordwreck.css")).toExternalForm());
        viewport.widthProperty().addListener((o,a,b) -> scalePage());
        viewport.heightProperty().addListener((o,a,b) -> scalePage());
        stage.setTitle("WORD-WRECK! | 8-BIT OCEAN QUEST");
        stage.setScene(scene);
        stage.setOnShown(e -> scalePage());
        stage.show();
        stage.centerOnScreen();
    }

    static void setRoot(String name) throws IOException {
        page = FXMLLoader.load(Objects.requireNonNull(App.class.getResource(name + ".fxml")));
        page.setMinSize(WIDTH, HEIGHT);
        page.setPrefSize(WIDTH, HEIGHT);
        page.setMaxSize(WIDTH, HEIGHT);
        viewport.getChildren().setAll(new Group(page));
        scalePage();
    }

    private static void scalePage() {
        if (page == null || viewport.getWidth() <= 0 || viewport.getHeight() <= 0) return;
        double scale = Math.min(viewport.getWidth()/WIDTH, viewport.getHeight()/HEIGHT);
        page.setScaleX(scale);
        page.setScaleY(scale);
    }

    static Stage window() { return window; }
    static boolean isExpanded() { return expanded; }

    // Explicit screen bounds also work for an undecorated, non-resizable stage.
    static void toggleMaximize() {
        if (!expanded) {
            oldX=window.getX(); oldY=window.getY(); oldW=window.getWidth(); oldH=window.getHeight();
            Screen screen = Screen.getScreensForRectangle(oldX+oldW/2, oldY+oldH/2, 1, 1)
                .stream().findFirst().orElse(Screen.getPrimary());
            Rectangle2D bounds = screen.getVisualBounds();
            window.setX(bounds.getMinX()); window.setY(bounds.getMinY());
            window.setWidth(bounds.getWidth()); window.setHeight(bounds.getHeight());
        } else {
            window.setX(oldX); window.setY(oldY);
            window.setWidth(oldW); window.setHeight(oldH);
        }
        expanded = !expanded;
    }
    public static void main(String[] args) { launch(args); }
}
