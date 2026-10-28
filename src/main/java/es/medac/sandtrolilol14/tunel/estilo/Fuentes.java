package es.medac.sandtrolilol14.tunel.estilo;

import java.io.InputStream;
import javafx.scene.text.Font;

public final class Fuentes {

    private static final String RUTA_BASE = "/es/medac/sandtrolilol14/tunel/fuentes/";

    private static final String[] ARCHIVOS_FUENTES = {
            "Creepster-Regular.ttf",
            "ShareTechMono-Regular.ttf"
    };

    private Fuentes() {
    }

    public static void cargar() {
        for (String nombreArchivo : ARCHIVOS_FUENTES) {
            cargarFuenteIndividual(nombreArchivo);
        }
    }

    private static void cargarFuenteIndividual(String nombreArchivo) {
        String rutaCompleta = RUTA_BASE + nombreArchivo;
        try (InputStream flujo = Fuentes.class.getResourceAsStream(rutaCompleta)) {
            if (flujo == null) {
                System.err.println("[Fuentes] Aviso: No se encontró el archivo de fuente en: " + rutaCompleta);
                return;
            }

            Font fuente = Font.loadFont(flujo, 20);
            if (fuente == null) {
                System.err.println("[Fuentes] Aviso: No se pudo cargar la fuente desde: " + rutaCompleta);
            } else {
                System.out.println("[Fuentes] Fuente cargada -> " + nombreArchivo
                        + " | Familia CSS: '" + fuente.getFamily() + "'");
            }
        } catch (Exception e) {
            System.err.println("[Fuentes] Aviso: Error al leer la fuente " + rutaCompleta + ": " + e.getMessage());
        }
    }
}