module project.womenshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.dlsc.formsfx;

    opens project.womenshop to javafx.fxml;
    exports project.womenshop;
}