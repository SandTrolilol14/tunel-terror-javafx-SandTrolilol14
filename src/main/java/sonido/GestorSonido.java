package sonido;

public class GestorSonido {
    public GestorSonido() {
        // Carga todo; si falta un archivo, avisa por consola y sigue sin ese sonido[cite: 13, 14]
    }

    public void iniciarAmbiente() {
        // Arranca los bucles de MediaPlayer (ambiente, estática, susurros)[cite: 13, 14]
    }

    public void actualizarSenal(double intensidad) {
        // 0.0 es lejos (mucha estática, sin susurros), 1.0 sintonizado (poca estática, susurros altos)[cite: 13, 14]
    }

    public void chasquido() {
        // AudioClip al mover el dial. Debe estar limitado (máximo uno cada 80 ms usando System.nanoTime)[cite: 13, 14]
    }

    public void reproducirVoz() {
        // La voz fantasma que dice la letra y baja temporalmente la estática[cite: 13, 14]
    }

    public void golpeSusto() {
        // AudioClip del golpe del susto
    }

    public void reiniciar() {
        // Restaura los volúmenes iniciales y para la voz
    }

    public void detenerTodo() {
        // Para y libera todos los recursos multimedia al cerrar la app
    }
}