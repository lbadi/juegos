package com.mygdx.game.networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 11/17/15.
 */
public class Inputs implements Serializable {

    private List<Input> inputs;
    private int playerId;

    public Inputs(int playerId) {
        inputs = new ArrayList<Input>();
        this.playerId = playerId;
    }

    public Inputs(List<Input> inputs, int playerId) {
        this.inputs = inputs;
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Inputs() {
        inputs = new ArrayList<Input>();
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void addInput(Input input) {
        inputs.add(input);
    }

    public void clear() {
        inputs.clear();
    }

}
