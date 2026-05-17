package swing2.view;

import javax.sound.sampled.*;
import java.net.URL;

public class ReproductorMusica {

    public void reproducirMusica(String ruta) {
        try {
            
            URL url = getClass().getResource(ruta);

            
            if (url == null) {
                url = getClass().getResource("/foto/" + ruta);
            }

            if (url != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                AudioFormat baseFormat = ais.getFormat();

                
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        44100,
                        false
                );

                AudioInputStream decodedAis = AudioSystem.getAudioInputStream(targetFormat, ais);
                Clip clip = AudioSystem.getClip();
                clip.open(decodedAis);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else {
                System.err.println("No se encontró el archivo de audio en: " + ruta);
            }

        } catch (Exception e) {
            System.err.println("Error al reproducir música: " + e.getMessage());
            e.printStackTrace();
        }
    }
}