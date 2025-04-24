module com.example.practicetwo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.practicetwo to javafx.fxml;
    exports com.example.practicetwo;
}