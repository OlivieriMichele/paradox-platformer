package com.project.paradoxplatformer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class SettingsPage extends AbstractThreadedPage {

    @FXML
    private Slider brightnessSlider;

    @FXML
    private Button toggleSoundButton;

    @FXML
    private Button saveButton;

    private boolean soundEnabled = true; // Example state

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindEvents();
    }

    private void bindEvents() {
        toggleSoundButton.setOnAction(e -> toggleSound());
        saveButton.setOnAction(e -> saveSettings());
    }

    private void toggleSound() {
        soundEnabled = !soundEnabled;
        // Logic to enable/disable sound
        System.out.println("Sound enabled: " + soundEnabled);
    }

    private void saveSettings() {
        double brightness = brightnessSlider.getValue();
        // Logic to save brightness settings
        System.out.println("Brightness set to: " + brightness);
    }

    @Override
    protected void runOnFXThread(String param) throws Exception {
        System.out.println("[Main Settings Panel]"); // Debug output or placeholder for actual logic
    }

}