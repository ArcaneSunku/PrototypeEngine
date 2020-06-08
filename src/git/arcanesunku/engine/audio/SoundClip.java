package git.arcanesunku.engine.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundClip {

    private Clip m_Clip = null;
    private FloatControl m_GainControl;

    public SoundClip(String path) {
        try {
            InputStream audioSource = SoundClip.class.getResourceAsStream("/assets/audio/" + path);
            InputStream bufferedIn = new BufferedInputStream(audioSource);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);

            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(), false);

            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);

            m_Clip = AudioSystem.getClip();
            m_Clip.open(dais);

            m_GainControl = (FloatControl) m_Clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.printf("Failed to load Sound Clip [%s]!%n", path);
            System.exit(-1);
        }
    }

    public void play() {
        if(m_Clip == null) return;

        stop();
        m_Clip.setFramePosition(0);

        while(!m_Clip.isRunning())
            m_Clip.start();
    }

    public void loop() {
        m_Clip.loop(Clip.LOOP_CONTINUOUSLY);
        play();
    }

    public void stop() {
        if(m_Clip.isRunning())
            m_Clip.stop();
    }

    public void close() {
        stop();

        m_Clip.drain();
        m_Clip.close();
    }

    public void setVolume(float value) {
        m_GainControl.setValue(value);
    }

    public boolean isRunning() {
        return m_Clip.isRunning();
    }

}
