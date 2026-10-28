package es.medac.sandtrolilol14.tunel;

import es.medac.sandtrolilol14.tunel.efectos.Efectos;
import es.medac.sandtrolilol14.tunel.logica.Sintonizador;
import es.medac.sandtrolilol14.tunel.sonido.GestorSonido;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controlador de la sala 2: Radio fantasma.
 * Solo conecta la vista con la lógica, el sonido y los efectos (cada uno en su propia clase).
 */
public class SalaController {

    @FXML private StackPane raiz;
    @FXML private VBox panelRadio;
    @FXML private Label lblTitulo;
    @FXML private Label lblInstruccion;
    @FXML private Label lblFrecuencia;
    @FXML private Slider sldDial;
    @FXML private ProgressBar pbSenal;
    @FXML private Label lblAyuda;
    @FXML private Label lblLetra;
    @FXML private ImageView imgSusto;

    private final Sintonizador sintonizador = new Sintonizador();
    private final GestorSonido sonido = new GestorSonido();

    @FXML
    private void initialize() {
        sldDial.setMin(Sintonizador.MINIMO);
        sldDial.setMax(Sintonizador.MAXIMO);
        reiniciar();
    }

    /** Devuelve la sala a su estado inicial para el siguiente grupo (también con F12). */
    public void reiniciar() {
        Efectos.detenerTodas();
        Efectos.ocultar(lblAyuda, lblLetra, imgSusto);
        raiz.getStyleClass().remove("sintonizada");
        sldDial.setValue(Sintonizador.MAXIMO);
        lblFrecuencia.setText("--.- MHz");
        pbSenal.setProgress(0);
        sonido.reiniciar();
    }

    /** Libera los recursos al cerrar la aplicación. */
    public void cerrar() {
        sonido.detenerTodo();
    }
}
