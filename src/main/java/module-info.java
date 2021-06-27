module YoGiOh {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;


    opens view.graphics.menus to javafx.fxml;
    exports view.graphics.menus;

}