package view.graphics.menus;


import controller.menus.MainMenuController;
import controller.menus.RegisterMenuController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class LoginMenuView {
    public Button backButton;
    public StackPane stackPane;
    public TextField usernameInput;
    public TextField passwordInput;

    public void openFirstPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomeMenu.fxml"));
        Scene scene = backButton.getScene();
        stackPane = (StackPane) scene.getRoot();
        root.translateXProperty().set(-1200);
        stackPane.getChildren().add(root);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), +1200, Interpolator.EASE_IN);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            scene.setRoot(root);
        });
    }

    public void openMainMenu() throws IOException {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        if (password.equals("") || username.equals(""))
            return;
        RegisterMenuController.getInstance().login(username, password);
        if (MainMenuController.getInstance().getPlayerLoggedIn() == null)
            return;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
        Scene scene = backButton.getScene();
        stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(root);
        root.translateYProperty().set(+480);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue nextPageKeyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateYProperty(), -480, Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            ((StackPane) scene.getRoot()).getChildren().add(root);
        });
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        HBox hBox = ((HBox) (((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(1)));
        ((Label) hBox.getChildren().get(0)).setText(MainMenuController.getInstance().getPlayerLoggedIn().getUsername());
    }
}