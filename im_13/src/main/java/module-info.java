module ru.vorotov.simulationslab13 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.vorotov.simulationslab13 to javafx.fxml;
    exports ru.vorotov.simulationslab13;
}