package com.project.paradoxplatformer.utils.effect;

import java.util.List;

import com.project.paradoxplatformer.utils.collision.ChainOfEffects;
import com.project.paradoxplatformer.utils.collision.api.CollisionType;
import com.project.paradoxplatformer.utils.effect.api.EffectHandlerFactory;
import com.project.paradoxplatformer.utils.geometries.coordinates.Coord2D;
import com.project.paradoxplatformer.utils.sound.SoundType;

public class EffectHandlerFactoryImpl implements EffectHandlerFactory {

    @Override
    public EffectHandler defaultEffectHandler() {
        EffectHandler handler = new EffectHandler();

        ChainOfEffects chain = ChainOfEffects.create(List.of(
                () -> new NoOpEffect(),
                () -> new SoundEffect(SoundType.OBSTACLE_HIT),
                () -> new TransportEffect(new Coord2D(100, 100), false)));

        handler.addCollisionEffectsForType(CollisionType.TRIGGER, chain);
        handler.addCollisionEffectsForType(CollisionType.DEATH_OBS, DeathEffect::new);
        handler.addCollisionEffectsForType(CollisionType.SPRINGS, SpringEffect::new);
        handler.addCollisionEffectsForType(CollisionType.SPRINGS, () -> new SoundEffect(SoundType.JUMP));
        handler.addCollisionEffectsForType(CollisionType.COLLECTING, new EffectFactoryImpl()::collectingEffect);
        handler.addCollisionEffectsForType(CollisionType.WALLS, new EffectFactoryImpl()::stoppingEffect);

        return handler;
    }

    @Override
    public EffectHandler levelOneEffectHandler() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'levelOneEffectHandler'");
    }

    @Override
    public EffectHandler levelTwoEffectHandler() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'levelTwoEffectHandler'");
    }

    @Override
    public EffectHandler levelThreeEffectHandler() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'levelThreeEffectHandler'");
    }

}