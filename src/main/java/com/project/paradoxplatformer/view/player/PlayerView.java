package com.project.paradoxplatformer.view.player;

import com.project.paradoxplatformer.model.player.PlayerModel;
import com.project.paradoxplatformer.utils.geometries.coordinates.Coord2D;

public class PlayerView {

    private PlayerModel model;
    //public Image playerImg;

    public PlayerView(PlayerModel model){
        this.model = model;
        // this.playerImg = ...;
    }

    public void display(){
        System.out.println(model.getPosition().toString());
    }

    public void updateState(Coord2D newPosition){
        // this.model.setPosition(newPosition);
    }
}
