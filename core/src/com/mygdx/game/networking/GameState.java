package com.mygdx.game.networking;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    private List<NetworkObject> objects;

    public GameState(List<NetworkObject> objects) {
        this.objects = objects;
    }

    public List<NetworkObject> getObjects() {
        return objects;
    }

}
