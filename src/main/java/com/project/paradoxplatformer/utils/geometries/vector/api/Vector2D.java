package com.project.paradoxplatformer.utils.geometries.vector.api;

import com.project.paradoxplatformer.utils.geometries.coordinates.Coord2D;

public sealed interface Vector2D permits AbstractVector{
    
    double magnitude();

    double direction();

    double yComponent();

    double xComponent();

    Vector2D add(Vector2D vector);

    Vector2D scalar(double scalar);

    Vector2D sub(Vector2D e);

    Coord2D convert();
    
}
