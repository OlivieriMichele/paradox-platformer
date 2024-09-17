package com.project.paradoxplatformer.model.player;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.project.paradoxplatformer.controller.gameloop.GameLoop;
import com.project.paradoxplatformer.model.effect.EffectFactoryImpl;
import com.project.paradoxplatformer.model.effect.EffectHandler;
import com.project.paradoxplatformer.model.effect.EffectHandlerImpl;
import com.project.paradoxplatformer.model.entity.CollectableGameObject;
import com.project.paradoxplatformer.model.obstacles.Coin;
import com.project.paradoxplatformer.utils.collision.CollisionManager;
import com.project.paradoxplatformer.utils.collision.api.CollidableGameObject;
import com.project.paradoxplatformer.utils.collision.api.CollisionType;
import com.project.paradoxplatformer.utils.geometries.Dimension;
import com.project.paradoxplatformer.utils.geometries.coordinates.Coord2D;

/**
 * Unit tests for SimpleInventory.
 */
public class SimpleInventoryTest {
    private static final double COIN_POSITION_X_2 = 50;
    private static final double COIN_POSITION_X_1 = 20;
    private static final int COIN_SIZE = 20;
    private static final int SIMULATION_STEP_TIME = 15;
    private static final int FIRST_LOOP_STEPS = 20;
    private static final int SECOND_LOOP_STEPS = 40;

    @Test
    void simpleCollectingItem() {

        PlayerModel player = new PlayerModel();
        CollectableGameObject coin = new Coin(Coord2D.origin(), Dimension.dot());

        player.collectItem(coin);

        System.out.println(player.getInventoryData());
        assertTrue(player.getInventoryData().get(Coin.class.getSimpleName()) == 1L);

        // Collects another coin
        CollectableGameObject coin2 = new Coin(Coord2D.origin(), Dimension.dot());
        player.collectItem(coin2);

        assertTrue(player.getInventoryData().get(Coin.class.getSimpleName()) == 2L);

        // Collects same coin
        player.collectItem(coin2);
        assertTrue(player.getInventoryData().get(Coin.class.getSimpleName()) == 2L);

    }

    @Test
    void collectingWithCollision() {

        final PlayerModel player = new PlayerModel();
        final CollectableGameObject coin = new Coin(new Coord2D(COIN_POSITION_X_1, 0),
                new Dimension(COIN_SIZE, COIN_SIZE));
        final CollectableGameObject coin2 = new Coin(new Coord2D(COIN_POSITION_X_2, 0),
                new Dimension(COIN_SIZE, COIN_SIZE));

        final EffectHandler effectHandler = new EffectHandlerImpl();
        effectHandler.addCollisionEffectsForType(CollisionType.COLLECTING, new EffectFactoryImpl()::collectingEffect);
        final CollisionManager collisionManager = new CollisionManager(effectHandler);

        final List<? extends CollidableGameObject> collidables = List.of(player, coin, coin2);

        // Main gameloop
        final GameLoop loop = dt -> {
            player.moveRight();
            player.updateState(dt);
            collisionManager.handleCollisions(collidables, player);
        };

        // Simulates a gameloop manager
        simulateLoop(loop, FIRST_LOOP_STEPS);

        // Prevents accessing to unexistent data
        assertFalse(player.getInventoryData().isEmpty());

        // Collects one coin
        assertTrue(player.getInventoryData().get(Coin.class.getSimpleName()) == 1L);

        simulateLoop(loop, SECOND_LOOP_STEPS);
        // Collects two different coins
        assertTrue(player.getInventoryData().get(Coin.class.getSimpleName()) == 2L);

    }

    private void simulateLoop(final GameLoop loop, final int steps) {
        IntStream.range(0, steps)
                .boxed()
                .map(i -> SIMULATION_STEP_TIME)
                .forEach(loop::loop);
    }
}
