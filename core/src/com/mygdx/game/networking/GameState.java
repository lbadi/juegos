package com.mygdx.game.networking;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    private List<NetworkObject> objects;

    private int frame;

    public GameState(List<NetworkObject> objects, int frame) {
        this.objects = objects;
        this.frame = frame;
    }

    public List<NetworkObject> getObjects() {
        return objects;
    }

    public int getFrame() {
        return frame;
    }
}
