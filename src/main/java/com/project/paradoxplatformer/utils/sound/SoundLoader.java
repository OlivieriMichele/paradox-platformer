package com.project.paradoxplatformer.utils.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import com.project.paradoxplatformer.utils.logging.GlobalLogger;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for loading and playing sound files asynchronously.
 */
public class SoundLoader {

    private final Logger logger = GlobalLogger.getLogger(SoundLoader.class);

    /**
     * Loads and plays a sound from the provided URL.
     * 
     * @param soundUrl The URL of the sound file to be played.
     * @return A CompletableFuture that completes when the sound has finished
     *         playing.
     */
    public CompletableFuture<Void> playSound(final URL soundUrl) {
        return CompletableFuture.runAsync(() -> {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl)) {
                final Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();

                // Wait for the clip to finish playing
                clip.drain();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                logger.error("Failed to load or play sound from URL: " + soundUrl, e);
            }
        });
    }
}
