package com.project.paradoxplatformer.utils.sound;

import javax.sound.sampled.*;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for loading and playing sound files asynchronously.
 */
public class SoundLoader {

    /**
     * Loads and plays a sound from the provided URL.
     * 
     * @param soundUrl The URL of the sound file to be played.
     * @return A CompletableFuture that completes when the sound has finished
     *         playing.
     */
    public CompletableFuture<Void> playSound(URL soundUrl) {
        return CompletableFuture.runAsync(() -> {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();

                // Wait for the clip to finish playing
                clip.drain();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException("Error playing sound", e);
            }
        });
    }
}
