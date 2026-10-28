package es.medac.sandtrolilol14.tunel;

import es.medac.sandtrolilol14.tunel.efectos.Efectos;
import es.medac.sandtrolilol14.tunel.logica.RelojPiedad;
import es.medac.sandtrolilol14.tunel.logica.Sintonizador;
import es.medac.sandtrolilol14.tunel.sonido.GestorSonido;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controlador de la sala 2: Radio fantasma.
 * Conecta la vista con la lógica (Sintonizador, RelojPiedad), el sonido y los efectos.
 */
public class SalaController {

    private static final Duration TIEMPO_AYUDA = Duration.seconds(60);
    private static final Duration TIEMPO_PIEDAD = Duration.seconds(90);
    private static final Duration LETRA_VISIBLE = Duration.seconds(10);

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
    private RelojPiedad relojAyuda;
    private RelojPiedad relojPiedad;

    /** true desde que el grupo toca el dial por primera vez. */
    private boolean partidaEmpezada;
    /** true mientras se muestra la letra: el dial deja de contar. */
    private boolean resuelta;
    /** true cuando el propio código mueve el dial (al reiniciar), para no tomarlo como un movimiento del visitante. */
    private boolean moviendoPorCodigo;

    @FXML
    private void initialize() {
        sldDial.setMin(Sintonizador.MINIMO);
        sldDial.setMax(Sintonizador.MAXIMO);

        relojAyuda = new RelojPiedad(TIEMPO_AYUDA, this::mostrarAyuda);
        relojPiedad = new RelojPiedad(TIEMPO_PIEDAD, this::apiadarse);

        sldDial.valueProperty().addListener((obs, antes, ahora) -> alMoverDial(ahora.doubleValue()));

        sonido.iniciarAmbiente();
        reiniciar();
    }

    /** Cada movimiento del dial: frecuencia, barra de señal, sonido y comprobación. */
    private void alMoverDial(double valor) {
        if (moviendoPorCodigo || resuelta) {
            return;
        }
        // Los relojes empiezan a contar cuando el grupo toca el dial por primera vez
        if (!partidaEmpezada) {
            partidaEmpezada = true;
            relojAyuda.iniciar();
            relojPiedad.iniciar();
        }

        double intensidad = sintonizador.intensidadSenal(valor);
        lblFrecuencia.setText(sintonizador.formatear(sintonizador.frecuenciaMostrada(valor)));
        pbSenal.setProgress(intensidad);
        sonido.actualizarSenal(intensidad);
        sonido.chasquido();

        if (sintonizador.estaSintonizada(valor)) {
            revelarLetra(false);
        }
    }

    /** A los 60 s sin resolver: pista. */
    private void mostrarAyuda() {
        if (resuelta) {
            return;
        }
        lblAyuda.setText(sintonizador.textoAyuda());
        Efectos.aparecer(lblAyuda);
    }

    /** A los 90 s sin resolver: la sala se apiada y da la letra. */
    private void apiadarse() {
        if (resuelta) {
            return;
        }
        lblAyuda.setText("La emisora se apiada de vosotros...");
        if (!lblAyuda.isVisible()) {
            Efectos.aparecer(lblAyuda);
        }
        revelarLetra(true);
    }

    /** Muestra la letra con efectos. Si han acertado (no por piedad), además hay susto controlado. */
    private void revelarLetra(boolean porPiedad) {
        resuelta = true;
        relojAyuda.parar();
        relojPiedad.parar();

        if (!raiz.getStyleClass().contains("sintonizada")) {
            raiz.getStyleClass().add("sintonizada");
        }
        lblFrecuencia.setText(sintonizador.formatear(Sintonizador.FRECUENCIA_OBJETIVO));
        pbSenal.setProgress(1);
        sonido.reproducirVoz();

        if (!porPiedad) {
            Efectos.temblar(panelRadio, 0.5);
            Efectos.sustoControlado(imgSusto);
            sonido.golpeSusto();
        }

        // Al terminar de mostrar la letra, la sala se reinicia sola para el siguiente grupo
        Efectos.revelarLetra(lblLetra, LETRA_VISIBLE, this::reiniciar);
    }

    /** Devuelve la sala a su estado inicial para el siguiente grupo (también con F12). */
    public void reiniciar() {
        resuelta = false;
        partidaEmpezada = false;
        if (relojAyuda != null) {
            relojAyuda.parar();
        }
        if (relojPiedad != null) {
            relojPiedad.parar();
        }

        Efectos.detenerTodas();
        Efectos.ocultar(lblAyuda, lblLetra, imgSusto);
        raiz.getStyleClass().remove("sintonizada");

        // Dial invertido: en el mínimo muestra 120.0 MHz, lejos de la frecuencia buscada
        moviendoPorCodigo = true;
        sldDial.setValue(Sintonizador.MINIMO);
        moviendoPorCodigo = false;

        lblFrecuencia.setText("--.- MHz");
        pbSenal.setProgress(0);
        sonido.reiniciar();
        Efectos.respirar(lblTitulo);
    }

    /** Libera los recursos al cerrar la aplicación. */
    public void cerrar() {
        if (relojAyuda != null) {
            relojAyuda.parar();
        }
        if (relojPiedad != null) {
            relojPiedad.parar();
        }
        Efectos.detenerTodas();
        sonido.detenerTodo();
    }
}