package es.medac.sandtrolilol14.tunel.efectos;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public final class Efectos {

    private static final double RESPIRAR_OPACIDAD_MAX = 1.0;
    private static final double RESPIRAR_OPACIDAD_MIN = 0.65;

    private static final Duration RESPIRAR_MEDIO_CICLO = Duration.seconds(1.5);

    private static final double TEMBLAR_PX_POR_FUERZA = 15;

    private static final Duration TEMBLAR_PASO = Duration.millis(80);

    private static final double[] TEMBLAR_FACTOR_X = {1.0, -1.0, 0.7, -0.4, 0.0};
    private static final double[] TEMBLAR_FACTOR_Y = {-0.5, 0.6, -0.3, 0.2, 0.0};


    private static final Duration APARECER_DURACION = Duration.seconds(1.2);


    private static final double SUSTO_ESCALA_INICIAL = 0.3;
    private static final double SUSTO_ESCALA_FINAL = 1.1;
    private static final Duration SUSTO_ENTRADA = Duration.seconds(0.35);
    private static final Duration SUSTO_PERMANENCIA = Duration.seconds(1.5);
    private static final Duration SUSTO_SALIDA = Duration.seconds(0.8);


    private static final double LETRA_ESCALA_INICIAL = 0.2;
    private static final Duration LETRA_ENTRADA = Duration.seconds(1.5);
    private static final Duration LETRA_SALIDA = Duration.seconds(1);

    private static final List<Animation> ANIMACIONES = new ArrayList<>();

    private static final Set<Node> NODOS = new LinkedHashSet<>();

    private Efectos() {
    }


    public static void respirar(Node nodo) {
        if (nodo == null) {
            return;
        }
        nodo.setOpacity(RESPIRAR_OPACIDAD_MAX);

        FadeTransition pulso = fundido(nodo, RESPIRAR_MEDIO_CICLO,
                RESPIRAR_OPACIDAD_MAX, RESPIRAR_OPACIDAD_MIN);
        pulso.setCycleCount(Animation.INDEFINITE);
        pulso.setAutoReverse(true); // baja a 0.65 y vuelve a 1.0, siempre suave

        lanzar(pulso, nodo);
    }


    public static void temblar(Node nodo, double fuerza) {
        if (nodo == null) {
            return;
        }
        double maximo = Math.max(0, fuerza) * TEMBLAR_PX_POR_FUERZA;

        SequentialTransition sacudida = new SequentialTransition();
        for (int i = 0; i < TEMBLAR_FACTOR_X.length; i++) {
            TranslateTransition paso = new TranslateTransition(TEMBLAR_PASO, nodo);
            paso.setToX(TEMBLAR_FACTOR_X[i] * maximo);
            paso.setToY(TEMBLAR_FACTOR_Y[i] * maximo);
            sacudida.getChildren().add(paso);
        }
        // Red de seguridad: pase lo que pase, el nodo acaba en su sitio.
        sacudida.setOnFinished(e -> {
            nodo.setTranslateX(0);
            nodo.setTranslateY(0);
        });

        lanzar(sacudida, nodo);
    }

    public static void aparecer(Node nodo) {
        if (nodo == null) {
            return;
        }
        nodo.setOpacity(0);
        nodo.setVisible(true);

        lanzar(fundido(nodo, APARECER_DURACION, 0, 1), nodo);
    }

    public static void revelarLetra(Node letra, Duration visible, Runnable alTerminar) {
        if (letra == null) {
            return;
        }

        Duration espera = (visible == null || visible.lessThan(Duration.ZERO))
                ? Duration.ZERO
                : visible;

        prepararEntrada(letra, LETRA_ESCALA_INICIAL);

        ParallelTransition entrada = new ParallelTransition(
                escala(letra, LETRA_ENTRADA, LETRA_ESCALA_INICIAL, 1.0),
                fundido(letra, LETRA_ENTRADA, 0, 1));

        PauseTransition pausa = new PauseTransition(espera);
        FadeTransition salida = fundido(letra, LETRA_SALIDA, 1, 0);

        SequentialTransition secuencia = new SequentialTransition(entrada, pausa, salida);
        secuencia.setOnFinished(e -> {
            ocultarUno(letra);
            if (alTerminar != null) {
                alTerminar.run();
            }
        });

        lanzar(secuencia, letra);
    }



    public static void sustoControlado(Node susto) {
        if (susto == null) {
            return;
        }
        prepararEntrada(susto, SUSTO_ESCALA_INICIAL);

        // Aparición: escala y fundido simultáneos
        ParallelTransition aparicion = new ParallelTransition(
                escala(susto, SUSTO_ENTRADA, SUSTO_ESCALA_INICIAL, SUSTO_ESCALA_FINAL),
                fundido(susto, SUSTO_ENTRADA, 0, 1));
        // Permanece en pantalla
        PauseTransition permanencia = new PauseTransition(SUSTO_PERMANENCIA);
        // Desvanecimiento
        FadeTransition salida = fundido(susto, SUSTO_SALIDA, 1, 0);

        SequentialTransition secuencia = new SequentialTransition(aparicion, permanencia, salida);
        secuencia.setOnFinished(e -> ocultarUno(susto)); // al final queda invisible

        lanzar(secuencia, susto);
    }

    public static void ocultar(Node... nodos) {
        if (nodos == null) {
            return;
        }
        for (Node nodo : nodos) {
            ocultarUno(nodo);
        }
    }

    public static void detenerTodas() {
        List<Animation> pendientes = new ArrayList<>(ANIMACIONES);
        List<Node> afectados = new ArrayList<>(NODOS);
        ANIMACIONES.clear();
        NODOS.clear();

        for (Animation animacion : pendientes) {
            animacion.stop(); // stop() NO ejecuta setOnFinished
        }
        for (Node nodo : afectados) {
            nodo.setTranslateX(0);
            nodo.setTranslateY(0);
            nodo.setScaleX(1);
            nodo.setScaleY(1);
            nodo.setOpacity(1);
        }
    }


    private static void lanzar(Animation animacion, Node nodo) {
        ANIMACIONES.add(animacion);
        NODOS.add(nodo);
        animacion.statusProperty().addListener((obs, antes, ahora) -> {
            if (ahora == Animation.Status.STOPPED) {
                ANIMACIONES.remove(animacion);
            }
        });
        animacion.play();
    }

    private static FadeTransition fundido(Node nodo, Duration duracion, double desde, double hasta) {
        FadeTransition fundido = new FadeTransition(duracion, nodo);
        fundido.setFromValue(desde);
        fundido.setToValue(hasta);
        return fundido;
    }

    private static ScaleTransition escala(Node nodo, Duration duracion, double desde, double hasta) {
        ScaleTransition escala = new ScaleTransition(duracion, nodo);
        escala.setFromX(desde);
        escala.setFromY(desde);
        escala.setToX(hasta);
        escala.setToY(hasta);
        return escala;
    }

    private static void prepararEntrada(Node nodo, double escalaInicial) {
        nodo.setOpacity(0);
        nodo.setScaleX(escalaInicial);
        nodo.setScaleY(escalaInicial);
        nodo.setVisible(true);
    }


    private static void ocultarUno(Node nodo) {
        if (nodo == null) {
            return;
        }
        nodo.setOpacity(0);
        nodo.setVisible(false);
        nodo.setScaleX(1);
        nodo.setScaleY(1);
        nodo.setTranslateX(0);
        nodo.setTranslateY(0);
    }
}