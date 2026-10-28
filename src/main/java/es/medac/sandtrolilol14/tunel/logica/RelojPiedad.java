package es.medac.sandtrolilol14.tunel.logica;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class RelojPiedad {

    private final PauseTransition pausa;

    public RelojPiedad(Duration espera, Runnable alAgotarse) {
        pausa = new PauseTransition(espera);
        pausa.setOnFinished(e -> alAgotarse.run());
    }

    public void iniciar() {
        pausa.playFromStart();
    }

    public void parar() {
        pausa.stop();
    }

    public boolean estaEnMarcha() {
        return pausa.getStatus() == javafx.animation.Animation.Status.RUNNING;
    }
}