package swing2.view;
import javax.sound.sampled.*;
import java.net.URL;

public class ReproductorMusica {
    private Clip clip;

    public void reproducirMusica(String ruta) {
        try {
            // Buscamos el archivo (debe ser .wav para máxima compatibilidad)
            URL url = getClass().getResource(ruta);
            if (url == null) {
                url = getClass().getResource("../.." + ruta);
            }

            if (url != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioInput);

                // Configuramos el bucle infinito
                clip.loop(Clip.LOOP_CONTINUOUSLY);

                // Ajustar el volumen (opcional, entre 0.0 y 1.0)
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f); // Baja un poco el volumen (en decibelios)

                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir música: " + e.getMessage());
        }
    }

    public void detenerMusica() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}