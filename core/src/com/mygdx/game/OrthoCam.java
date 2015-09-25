package com.mygdx.game;

import projection.OrthoProjection;

import com.badlogic.gdx.math.Matrix4;

public class OrthoCam extends Cam {

    private OrthoProjection projection = new OrthoProjection();

    // Ver pagina 91 del libro
    //TODO ARREGLAR ESTA CAMARA
    public void setProjection(float l, float r, float b, float t, float n, float f) {
        projection.setProjection(l, r, b, t, n, f);
    }

    // Projection matrix P
    public Matrix4 getProjectionMatrix() {
        return projection.getProjectionMatrix();
    }

}
