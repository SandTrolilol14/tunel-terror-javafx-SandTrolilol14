package es.medac.sandtrolilol14.tunel.sonido;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * GestorSonido se encarga de administrar y reproducir todos los efectos de audio
 * y pistas ambientales de la sala de la radio fantasma mediante JavaFX Media.
 */
public class GestorSonido {

    // Reproductores en bucle (MediaPlayer) declarados como atributos para evitar que el recolector de basura los elimine.
    private MediaPlayer ambientePlayer;
    private MediaPlayer estaticaPlayer;
    private MediaPlayer susurrosPlayer;
    private MediaPlayer vozPlayer;

    // Efectos cortos (AudioClip)
    private AudioClip chasquidoClip;
    private AudioClip sustoClip;

    // Control de tiempo para limitar la frecuencia del chasquido del dial
    private long ultimaVezChasquido = 0;
    private static final long INTERVALO_MINIMO_NS = 80_000_000L; // 80 milisegundos en nanosegundos

    /**
     * Constructor: carga todos los recursos de audio de forma segura.
     * Si algún archivo falla o no se encuentra, avisa por consola pero permite que la app continúe.
     */
    public GestorSonido() {
        // Cargar ambiente.mp3
        try {
            var url = GestorSonido.class.getResource("/es/medac/sandtrolilol14/tunel/audio/ambiente.mp3");
            if (url != null) {
                ambientePlayer = new MediaPlayer(new Media(url.toExternalForm()));
                ambientePlayer.setCycleCount(MediaPlayer.INDEFINITE);
                ambientePlayer.setVolume(0.45);
            } else {
                System.err.println("Aviso: No se encontró el archivo de audio ambiente.mp3");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar ambiente.mp3: " + e.getMessage());
        }

        // Cargar estatica.mp3
        try {
            var url = GestorSonido.class.getResource("/es/medac/sandtrolilol14/tunel/audio/estatica.mp3");
            if (url != null) {
                estaticaPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                estaticaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                estaticaPlayer.setVolume(0.8);
            } else {
                System.err.println("Aviso: No se encontró el archivo de audio estatica.mp3");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar estatica.mp3: " + e.getMessage());
        }

        // Cargar susurros.mp3
        try {
            var url = GestorSonido.class.getResource("/es/medac/sandtrolilol14/tunel/audio/susurros.mp3");
            if (url != null) {
                susurrosPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                susurrosPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                susurrosPlayer.setVolume(0.0);
            } else {
                System.err.println("Aviso: No se encontró el archivo de audio susurros.mp3");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar susurros.mp3: " + e.getMessage());
        }

        // Cargar voz.mp3
        try {
            var url = GestorSonido.class.getResource("/es/medac/sandtrolilol14/tunel/audio/voz.mp3");
            if (url != null) {
                vozPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                vozPlayer.setVolume(0.8);
            } else {
                System.err.println("Aviso: No se encontró el archivo de audio voz.mp3");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar voz.mp3: " + e.getMessage());
        }

        // Cargar chasquido.mp3 (AudioClip)
        try {
            var url = GestorSonido.class.getResource("/es/medac/sandtrolilol14/tunel/audio/chasquido.mp3");
            if (url != null) {
                chasquidoClip = new AudioClip(url.toExternalForm());
            } else {
                System.err.println("Aviso: No se encontró el archivo de audio chasquido.mp3");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar chasquido.mp3: " + e.getMessage());
        }

        // Cargar susto.mp3 (AudioClip)
        try {
            var url = GestorSonido.class.getResource("/es/medac/sandtrolilol14/tunel/audio/susto.mp3");
            if (url != null) {
                sustoClip = new AudioClip(url.toExternalForm());
            } else {
                System.err.println("Aviso: No se encontró el archivo de audio susto.mp3");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar susto.mp3: " + e.getMessage());
        }
    }

    /**
     * Arranca los bucles ambientales principales de la radio.
     */
    public void iniciarAmbiente() {
        if (ambientePlayer != null) {
            ambientePlayer.play();
        }
        if (estaticaPlayer != null) {
            estaticaPlayer.setVolume(0.8);
            estaticaPlayer.play();
        }
        if (susurrosPlayer != null) {
            susurrosPlayer.setVolume(0.0);
            susurrosPlayer.play();
        }
    }

    /**
     * Actualiza el volumen de la estática y los susurros en función de la proximidad a la frecuencia correcta.
     * @param intensidad Valor entre 0.0 (muy lejos) y 1.0 (totalmente sintonizado).
     */
    public void actualizarSenal(double intensidad) {
        // Limitar la intensidad estrictamente entre 0 y 1
        double intensidadLimitada = Math.max(0.0, Math.min(1.0, intensidad));

        // Verificamos si la voz fantasma está sonando actualmente
        boolean vozSonando = (vozPlayer != null && vozPlayer.getStatus() == MediaPlayer.Status.PLAYING);

        // Estática: disminuye conforme nos acercamos al objetivo
        if (estaticaPlayer != null) {
            if (vozSonando) {
                estaticaPlayer.setVolume(0.05);
            } else {
                double volEstatica = 0.8 * (1.0 - intensidadLimitada);
                estaticaPlayer.setVolume(Math.min(0.8, volEstatica));
            }
        }

        // Susurros: aumentan conforme nos acercamos al objetivo
        if (susurrosPlayer != null) {
            double volSusurros = 0.8 * intensidadLimitada;
            susurrosPlayer.setVolume(Math.min(0.8, volSusurros));
        }
    }

    /**
     * Reproduce el sonido de chasquido al mover el dial, limitado a una vez cada 80 ms.
     */
    public void chasquido() {
        if (chasquidoClip != null) {
            long ahora = System.nanoTime();
            if (ahora - ultimaVezChasquido >= INTERVALO_MINIMO_NS) {
                chasquidoClip.play(0.4);
                ultimaVezChasquido = ahora;
            }
        }
    }

    /**
     * Reproduce la voz fantasma que revela la letra y reduce la estática de fondo.
     */
    public void reproducirVoz() {
        if (vozPlayer != null) {
            vozPlayer.stop();
            vozPlayer.seek(Duration.ZERO);
            vozPlayer.setVolume(0.8);
            vozPlayer.play();
        }
        if (estaticaPlayer != null) {
            estaticaPlayer.setVolume(0.05);
        }
    }

    /**
     * Reproduce el efecto sonoro del susto controlado.
     */
    public void golpeSusto() {
        if (sustoClip != null) {
            sustoClip.play(0.7);
        }
    }

    /**
     * Restaura los valores iniciales y detiene la voz.
     */
    public void reiniciar() {
        if (vozPlayer != null) {
            vozPlayer.stop();
        }
        actualizarSenal(0.0);
    }

    /**
     * Detiene y libera todos los recursos multimedia al cerrar la aplicación.
     */
    public void detenerTodo() {
        if (ambientePlayer != null) {
            ambientePlayer.stop();
            ambientePlayer.dispose();
        }
        if (estaticaPlayer != null) {
            estaticaPlayer.stop();
            estaticaPlayer.dispose();
        }
        if (susurrosPlayer != null) {
            susurrosPlayer.stop();
            susurrosPlayer.dispose();
        }
        if (vozPlayer != null) {
            vozPlayer.stop();
            vozPlayer.dispose();
        }
    }
}