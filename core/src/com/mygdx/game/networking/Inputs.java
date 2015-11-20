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

    public Inputs(List<Input> inputs) {
        this.inputs = inputs;
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
