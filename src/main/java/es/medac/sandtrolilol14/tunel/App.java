package es.medac.sandtrolilol14.tunel;

import es.medac.sandtrolilol14.tunel.estilo.Fuentes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Punto de entrada de la sala 2: Radio fantasma.
 * Carga la fuente, la vista FXML y la hoja de estilos, y pone la ventana a pantalla completa.
 */
public class App extends Application {

    /** Usuario de GitHub del propietario del repositorio (se muestra en el título). */
    public static final String PROPIETARIO = "SandTrolilol14";

    /** Combinación secreta para que el equipo pueda cerrar la sala (los visitantes no la conocen). */
    private static final KeyCombination SALIR =
            new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

    private SalaController controlador;

    @Override
    public void start(Stage stage) throws Exception {
        // La fuente tiene que estar cargada antes de aplicar el CSS que la usa
        Fuentes.cargar();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("sala.fxml"));
        Parent raiz = loader.load();
        controlador = loader.getController();

        Scene escena = new Scene(raiz, 1280, 720);
        escena.getStylesheets().add(App.class.getResource("estilos.css").toExternalForm());

        // F12: reinicio manual para el siguiente grupo. Ctrl+Shift+Q: salir (solo el equipo).
        escena.setOnKeyPressed(evento -> {
            if (evento.getCode() == KeyCode.F12) {
                controlador.reiniciar();
            } else if (SALIR.match(evento)) {
                cerrar();
            }
        });

        stage.setTitle("Radio fantasma · " + PROPIETARIO);
        stage.setScene(escena);

        // Pantalla completa sin aviso y sin salida accidental con Esc
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);

        stage.setOnCloseRequest(evento -> controlador.cerrar());
        stage.show();
    }

    private void cerrar() {
        controlador.cerrar();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
