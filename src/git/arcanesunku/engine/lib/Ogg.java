package git.arcanesunku.engine.lib;

import org.newdawn.easyogg.OggClip;

import java.io.IOException;
import java.net.URL;

/**
 * @author Tom
 *
 * (http://www.java-gaming.org/index.php?action=profile;u=5098}
 * Wraps an OggClip to avoid the threading bugs in OggClip. The wrapper can be
 * played more than once at a time without problem.
 */

public class Ogg {

    private OggClip m_Clip;
    private URL m_Resource;
    private float m_Gain;
    private boolean m_Looping = false;

    public Ogg(URL resource, float gain) {
        this.m_Resource = resource;
        this.m_Gain = gain;
    }

    public void play() {
        if (m_Clip != null && m_Looping) {
            m_Clip.stop();
        }
        m_Looping = false;

        try {
            m_Clip = new OggClip(m_Resource.openStream());
            m_Clip.setGain(m_Gain);
            m_Clip.play();
        } catch (IOException e) {
            e.printStackTrace();
            m_Clip = null;
        }
    }

    public void stop() {
        if (m_Clip != null) {
            m_Looping = false;
            m_Clip.stop();
            m_Clip = null;
        }
    }

    public void loop() {
        if (m_Clip != null && m_Looping) {
            m_Clip.stop();
        }

        try {
            m_Clip = new OggClip(m_Resource.openStream());
            m_Clip.setGain(m_Gain);
            m_Clip.loop();
            m_Looping = true;
        } catch (IOException e) {
            e.printStackTrace();
            m_Clip = null;
        }
    }

    public void pause() {
        if (m_Clip != null) {
            m_Clip.pause();
        }
    }

    public void resume() {
        if (m_Clip != null) {
            m_Clip.resume();
        }
    }

    public float getGain() {
        return m_Gain;
    }

    /**
     * Handles the base gain with a value from 0.0 to 1.0
     *
     * @param gain the amount of gain the audio file should have for playback
     */
    public void setGain(float gain) {
        m_Gain = gain;

        if (m_Gain < 0.0)
            m_Gain = 0.0f;
        else if (m_Gain > 1.0)
            m_Gain = 1.0f;

        m_Clip.setGain(m_Gain);
    }

    public int getVolume() {
        return (int) (m_Gain * 100);
    }

    /**
     * Handles the gain in a "volume" manner. From 0 - 100
     *
     * @param volume measures gain from 0 - 100 instead
     */
    public void setVolume(int volume) {
        setGain(volume / 100f);
    }
}