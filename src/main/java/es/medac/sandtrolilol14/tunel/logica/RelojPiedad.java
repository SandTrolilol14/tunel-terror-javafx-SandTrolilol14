package es.medac.sandtrolilol14.tunel.logica;

import javafx.util.Duration;

/**
 * Cuenta atrás que avisa cuando pasa un tiempo sin resolver la sala (ayuda a los 60 s, letra a los 90 s).
 * PARTE 4 (lógica): implementar con PauseTransition. Las firmas públicas no se cambian.
 */
public class RelojPiedad {

    private final Duration espera;
    private final Runnable alAgotarse;

    public RelojPiedad(Duration espera, Runnable alAgotarse) {
        this.espera = espera;
        this.alAgotarse = alAgotarse;
    }

    /** Empieza (o reinicia) la cuenta desde cero. */
    public void iniciar() {
        // TODO parte 4
    }

    /** Detiene la cuenta sin ejecutar nada. */
    public void parar() {
        // TODO parte 4
    }

    public boolean estaEnMarcha() {
        return false; // TODO parte 4
    }
}
