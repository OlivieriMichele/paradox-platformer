package com.project.paradoxplatformer.controller.games;

import com.project.paradoxplatformer.controller.deserialization.dtos.GameDTO;
import com.project.paradoxplatformer.model.entity.MutableObject;
import com.project.paradoxplatformer.model.world.GameModelData;
import com.project.paradoxplatformer.model.world.api.World;
import com.project.paradoxplatformer.utils.InvalidResourceException;
import com.project.paradoxplatformer.utils.geometries.Dimension;
import com.project.paradoxplatformer.utils.geometries.coordinates.Coord2D;
import com.project.paradoxplatformer.view.game.GameView;
import com.project.paradoxplatformer.view.graphics.GraphicAdapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

public class GameControllerImpl implements GameController{

    private final GameModelData gameModel;
    private Map<MutableObject, GraphicAdapter> gamePair;
    private final GameView gameView;
    private Function<GraphicAdapter, Coord2D> position;
    private Function<GraphicAdapter, Dimension> dimension;

    public GameControllerImpl(final GameModelData model, final GameView view) {
        this.gameModel = model;
        this.gameView = view;
        this.gamePair = new HashMap<>();
        position = GraphicAdapter::relativePosition;
        dimension = GraphicAdapter::dimension;
        var h = FXCollections.emptyObservableMap();
        
    }

    @Override
    public void loadModel() {
        gameModel.init();
    }

    //Need abstraction for view creation
    @Override
    public void syncView() throws InvalidResourceException{
        
        this.gameView.init();
        gamePair = this.gameView.getControls().stream()
            //FIX DUPLICATE KEYYS
            .map(g -> this.join(g, this.gameModel.getWorld()))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));            
    }

    private Pair<MutableObject, GraphicAdapter> join(GraphicAdapter g, World world) {
        //SHOULD GET FROM WORLD, JUST TO MAKE THINGS EASY
        //MAKE A CONCAT OF ALL ENTITIES
        final Set<MutableObject> str = Stream.concat(this.gameModel.getWorld().obstacles().stream(),
            Stream.of(this.gameModel.getWorld().player())).collect(Collectors.toSet());
        return str.stream()
            .filter(m -> this.joinPredicate(m, g))
            .map(m -> Pair.of(m, g))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                    "Failed to pair object and graphic\nCause: " +
                    "\nGraphic → " + dimension.apply(g) +
                    "\nGraphic → " + position.apply(g) 
                )
            );
    }


    private boolean joinPredicate(final MutableObject obstacle1, GraphicAdapter gComponent) {
        return obstacle1.getDimension().equals(dimension.apply(gComponent)) 
            && obstacle1.getPosition().equals(position.apply(gComponent));
        
    }

    @Override
    public void update(final long dt) {
        if(Objects.nonNull(gamePair)) {
            gamePair.forEach((m, g) -> m.updateState(dt));
            gamePair.forEach(gameView::updateEnitityState);
            
        }
    }

    
}