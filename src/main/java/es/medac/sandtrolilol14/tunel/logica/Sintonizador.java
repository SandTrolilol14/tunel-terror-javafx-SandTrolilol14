package es.medac.sandtrolilol14.tunel.logica;

import java.util.Locale;

public class Sintonizador {

    public static final double FRECUENCIA_OBJETIVO = 66.6;
    public static final double MINIMO = 60.0;
    public static final double MAXIMO = 120.0;
    public static final double TOLERANCIA = 0.3;

    public Sintonizador() {
        if (FRECUENCIA_OBJETIVO < MINIMO || FRECUENCIA_OBJETIVO > MAXIMO) {
            throw new IllegalStateException("La frecuencia objetivo debe estar entre el mínimo y el máximo permitidos.");
        }
    }

    public double frecuenciaMostrada(double valorDial) {
        return MINIMO + MAXIMO - valorDial;
    }

    public double intensidadSenal(double valorDial) {
        double freqActual = frecuenciaMostrada(valorDial);
        double distancia = Math.abs(freqActual - FRECUENCIA_OBJETIVO);

        if (distancia <= TOLERANCIA) {
            return 1.0;
        }

        double rangoEfectivo = 8.0; // A unos 8 MHz la señal llega a 0
        if (distancia >= rangoEfectivo) {
            return 0.0;
        }

        // Curva suave descendente
        double intensidad = 1.0 - ((distancia - TOLERANCIA) / (rangoEfectivo - TOLERANCIA));
        return Math.max(0.0, Math.min(1.0, intensidad));
    }

    public boolean estaSintonizada(double valorDial) {
        double freqActual = frecuenciaMostrada(valorDial);
        return Math.abs(freqActual - FRECUENCIA_OBJETIVO) <= TOLERANCIA;
    }

    public String formatear(double frecuencia) {
        return String.format(Locale.US, "%.1f MHz", frecuencia);
    }

    public String textoAyuda() {
        int decenaInferior = (int) (FRECUENCIA_OBJETIVO / 10) * 10;
        int decenaSuperior = decenaInferior + 10;
        return String.format("Pista: La frecuencia está entre %d y %d. Recuerda que el dial miente y va al revés...", decenaInferior, decenaSuperior);
    }
}