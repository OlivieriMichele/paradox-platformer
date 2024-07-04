package com.project.paradoxplatformer.utils.geometries.vector.api;

public interface Vector2D {
    
    double magnitude();

    double direction();

    double yComponent();

    double xComponent();

    Vector2D add(Vector2D vector);

    Vector2D scalar(double scalar);

    Vector2D sub(Vector2D e);
    
}
