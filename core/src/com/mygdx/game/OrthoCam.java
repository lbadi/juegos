package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;

public class OrthoCam extends Cam {

    private Matrix4 projection;

    // Ver pagina 91 del libro
    public void setProjection(float l, float r, float b, float t, float n, float f) {
        float[] values = {  2/(r-l), 0, 0, -(r+l)/(r-l),
                            0, 2/(t-b), 0, -(t+b)/(t-b),
                            0, 0, 2/(f-n), -(f+n)/(f-n),
                            0, 0, 0, 1};
        projection = new Matrix4(values).tra();
    }

    // Projection matrix P
    public Matrix4 getProjectionMatrix() {
        return projection;
    }

}
