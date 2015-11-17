package com.mygdx.game.networking;

import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;


public class NetworkObject implements Serializable {

    public int id;
    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    public NetworkObject(int id, Vector3 position, Vector3 rotation, Vector3 scale) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

}
