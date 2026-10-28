package es.medac.sandtrolilol14.tunel.logica;

import java.util.Locale;

/**
 * Lógica del dial de la radio (Java puro, sin JavaFX).
 * PARTE 4 (lógica): implementar los métodos. Las firmas públicas no se cambian.
 */
public class Sintonizador {

    /** Frecuencia que da la página HTML de 1.º. */
    public static final double FRECUENCIA_OBJETIVO = 66.6;
    public static final double MINIMO = 60.0;
    public static final double MAXIMO = 120.0;
    public static final double TOLERANCIA = 0.3;

    /** Valor del Slider → frecuencia que se muestra (dial invertido). */
    public double frecuenciaMostrada(double valorDial) {
        return valorDial; // TODO parte 4
    }

    /** 0.0 lejos ... 1.0 justo en la frecuencia. */
    public double intensidadSenal(double valorDial) {
        return 0.0; // TODO parte 4
    }

    /** true si la frecuencia mostrada está dentro de la tolerancia. */
    public boolean estaSintonizada(double valorDial) {
        return false; // TODO parte 4
    }

    /** Formato "66.6 MHz". */
    public String formatear(double frecuencia) {
        return String.format(Locale.US, "%.1f MHz", frecuencia);
    }

    /** Pista que se muestra a los 60 s. */
    public String textoAyuda() {
        return ""; // TODO parte 4
    }
}
