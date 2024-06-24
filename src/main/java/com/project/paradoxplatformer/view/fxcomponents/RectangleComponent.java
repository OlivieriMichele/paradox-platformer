package com.project.paradoxplatformer.view.fxcomponents;

import java.util.Optional;

import com.project.paradoxplatformer.utils.geometries.Dimension;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleComponent extends AbstractGraphicComponent {

    private Rectangle blockComponent;
    
    public RectangleComponent(Node component, Dimension dimension, Color fill) {
        super(component, dimension);
        if (component instanceof Rectangle blockCopy) {
            this.blockComponent = blockCopy;
            this.blockComponent.setFill(fill);
            this.setDimension(dimension.width(), dimension.height());
            
        } else {
            throw new IllegalArgumentException("Require rectangle");
        }
    }

    @Override
    public void setDimension(double width, double height) {
        this.blockComponent.setWidth(width);
        this.blockComponent.setHeight(height);
    }

    @Override
    public Node unwrap() {
        return this.blockComponent;
    }

    @Override
    public Optional<Image> image() {
        return Optional.empty();
    }

    @Override
    public Optional<Color> color() {
        return Optional.of((Color)this.blockComponent.getFill());
    }

}
