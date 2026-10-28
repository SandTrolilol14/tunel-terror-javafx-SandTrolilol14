module es.medac.sandtrolilol14.tunel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    // FXMLLoader necesita acceder por reflexión a los campos @FXML del controlador
    opens es.medac.sandtrolilol14.tunel to javafx.fxml;
    exports es.medac.sandtrolilol14.tunel;
}
