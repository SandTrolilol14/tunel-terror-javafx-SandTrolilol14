package es.medac.sandtrolilol14.tunel.efectos;

import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animaciones de la sala (Transition / Timeline). Sin destellos ni parpadeos rápidos.
 * PARTE 6 (efectos): implementar. Las firmas públicas no se cambian.
 */
public final class Efectos {

    private Efectos() {
    }

    /** Pulso lento e infinito de opacidad. */
    public static void respirar(Node nodo) {
        // TODO parte 6
    }

    /** Sacudida corta; fuerza de 0 a 1. */
    public static void temblar(Node nodo, double fuerza) {
        // TODO parte 6
    }

    /** Fundido de entrada suave. */
    public static void aparecer(Node nodo) {
        nodo.setVisible(true); // provisional
        nodo.setOpacity(1);    // TODO parte 6
    }

    /** Escala + fundido de la letra, la mantiene "visible" y luego ejecuta alTerminar. */
    public static void revelarLetra(Node letra, Duration visible, Runnable alTerminar) {
        letra.setVisible(true); // provisional
        letra.setOpacity(1);    // TODO parte 6
    }

    /** Aparición por escala + fundido, sin parpadeos; desaparece sola. */
    public static void sustoControlado(Node susto) {
        // TODO parte 6
    }

    /** Opacidad 0, invisible, escala 1 y translate 0. */
    public static void ocultar(Node... nodos) {
        for (Node nodo : nodos) { // provisional, TODO parte 6
            nodo.setOpacity(0);
            nodo.setVisible(false);
        }
    }

    /** Para todas las animaciones lanzadas desde aquí y deja los nodos en posición neutra. */
    public static void detenerTodas() {
        // TODO parte 6
    }
}
