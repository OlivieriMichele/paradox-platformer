package com.project.paradoxplatformer.view.page;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.project.paradoxplatformer.controller.games.Level;
import com.project.paradoxplatformer.utils.InvalidResourceException;
import com.project.paradoxplatformer.view.legacy.ViewFramework;

import javafx.application.Platform;
import javafx.fxml.Initializable;

/**
 * Abstract base class for pages that need to run code on the JavaFX application
 * thread.
 * <p>
 * This class ensures that the provided code is executed on the JavaFX
 * application thread,
 * either by scheduling it on the thread if it is not currently on it or
 * executing it directly
 * if it is already on the JavaFX thread.
 * </p>
 */
public abstract class AbstractThreadedPage implements Page<Level>, Initializable {

    /**
     * Initializes the page with the given location and resources.
     * <p>
     * This method must be implemented by subclasses to provide initialization logic
     * for the page.
     * </p>
     *
     * @param location  the location used to resolve relative paths for the root
     *                  object
     * @param resources the resources used to localize the root object
     */
    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

    /**
     * Creates and initializes the page with the given parameter.
     * <p>
     * This method ensures that the code is executed on the JavaFX application
     * thread. If the code
     * is not already on the JavaFX thread, it will be scheduled to run on it.
     * Subclasses should
     * implement the {@link #runOnFXThread(Level)} method to define the specific
     * actions to be
     * performed on the JavaFX thread.
     * </p>
     *
     * @param param the parameter used for initialization; must be final to ensure
     *              thread safety
     * @throws Exception if any error occurs while running the code on the JavaFX
     *                   thread
     */
    @Override
    public void create(final Level param) throws IOException, InvalidResourceException {
        // Ensure that the code runs on the JavaFX application thread
        if (!Platform.isFxApplicationThread()) {
            ViewFramework.javaFxFactory().mainAppManager().get().runOnAppThread(() -> safelyRunOnFXThread(param));
        } else {
            safelyRunOnFXThread(param);
        }
    }

    /**
     * Safely runs the provided code on the JavaFX application thread.
     * <p>
     * This method handles exceptions that may occur while running the code on the
     * JavaFX thread
     * and wraps them in an {@link IllegalStateException}.
     * </p>
     *
     * @param param the parameter used for initialization; must be final to ensure
     *              thread safety
     */
    private void safelyRunOnFXThread(final Level param) {
        try {
            this.runOnFXThread(param); // Run the given code on the JavaFX thread
        } catch (Exception e) { // NOPMD
            throw new IllegalStateException(e.getMessage(), e); // Wrap and rethrow exception
        }
    }

    /**
     * Defines the actions to be performed on the JavaFX application thread.
     * <p>
     * Subclasses should implement this method to provide the specific logic that
     * needs to be
     * executed on the JavaFX thread.
     * </p>
     *
     * @param param the parameter used for initialization
     * @throws Exception if any error occurs while running the code on the JavaFX
     *                   thread
     */
    protected abstract void runOnFXThread(Level param) throws Exception; // NOPMD (it is necessary)

}
