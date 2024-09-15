package com.project.paradoxplatformer.controller.input;

import com.project.paradoxplatformer.controller.input.api.InputType;
import com.project.paradoxplatformer.controller.input.api.KeyAssetter;
import com.project.paradoxplatformer.controller.input.api.KeyInputer;
import com.project.paradoxplatformer.model.entity.dynamics.ControllableObject;
import com.project.paradoxplatformer.model.entity.dynamics.behavior.PlatformJump;
import com.project.paradoxplatformer.model.inputmodel.InputMovesFactoryImpl;
import com.project.paradoxplatformer.model.player.PlayerModel;
import com.project.paradoxplatformer.utils.geometries.Dimension;
import com.project.paradoxplatformer.utils.geometries.coordinates.Coord2D;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Test class for InputController.
 * This class contains unit tests to verify multi-key input handling for
 * controlling the player in the game.
 */
public class InputControllerTest {

    private static final int PLAYER_DIMENSION = 30;
    private static final int UPDATE_STATE_INTERVAL = 15;

    /**
     * Tests the InputController's ability to handle simultaneous jump and movement
     * key presses.
     * Simulates pressing both the jump key ("W") and the movement key ("D") and
     * verifies that
     * the player can move and jump simultaneously.
     */
    @Test
    void testMultiKey() {

        var factory = new InputMovesFactoryImpl();
        var inputController = new InputController<>(factory.wasdModel());

        PlayerModel player = new PlayerModel(new Coord2D(0, 0), new Dimension(PLAYER_DIMENSION, PLAYER_DIMENSION));
        player.setJumpBehavior(new PlatformJump());

        // Simulate a jump and movement to the right
        KeyInputerImpl keyInput = new KeyInputerImpl();
        keyInput.simulateKeyAdd("D");
        keyInput.simulateKeyAdd("W");

        // First frame update to process input
        inputController.checkPool(keyInput.getKeyAssetter(), player, ControllableObject::stop);
        player.updateState(UPDATE_STATE_INTERVAL);

        // Register the position after movement and jumping
        var prevPos = registerPosition(player);
        assertTrue(prevPos.x() > 0. && prevPos.y() > 0, "Player should have moved to the right and jumped.");

        // Remove the keys to simulate the stopping of movement
        keyInput.simulateKeyRemove("D");
        keyInput.simulateKeyRemove("W");

        // Second frame update to process input after keys are released
        inputController.checkPool(keyInput.getKeyAssetter(), player, ControllableObject::stop);
        player.updateState(UPDATE_STATE_INTERVAL);

        // Verify that the player stopped moving horizontally but is still jumping
        var stoppingPos = registerPosition(player);
        assertTrue(prevPos.x() == stoppingPos.x(), "Player should stop horizontal movement.");
    }

    /**
     * Helper method to register the player's current position.
     * 
     * @param player the player model
     * @return the current position of the player
     */
    private Coord2D registerPosition(final PlayerModel player) {
        return player.getPosition();
    }

    /**
     * KeyInputer implementation to simulate adding and removing keys for input
     * control.
     * It is used in the unit tests to simulate key presses.
     */
    private class KeyInputerImpl implements KeyInputer<String> {

        private final KeyAssetter<String> keyassetter;

        /**
         * Constructor that initializes the KeyAssetter.
         */

        KeyInputerImpl() {
            this.keyassetter = new KeyAssetterImpl<>(InputType::getString);
        }

        /**
         * Simulates adding a key to the input pool.
         * 
         * @param key the key to add
         */
        public void simulateKeyAdd(final String key) {
            this.keyassetter.add(key);
        }

        /**
         * Simulates removing a key from the input pool.
         * 
         * @param key the key to remove
         */
        public void simulateKeyRemove(final String key) {
            this.keyassetter.remove(key);
        }

        @Override
        public KeyAssetter<String> getKeyAssetter() {
            return new KeyAssetterImpl<>(this.keyassetter);
        }

        @Override
        public void activateKeyInput(final Runnable activateInput) {
            // No-op implementation
        }
    }
}
