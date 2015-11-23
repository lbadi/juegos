package com.mygdx.game.networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 11/17/15.
 */
public class Inputs implements Serializable {

    private List<Integer> inputs;
    private int playerId;

    public Inputs(int playerId) {
        inputs = new ArrayList<Integer>();
        this.playerId = playerId;
    }

    public Inputs(List<Integer> inputs, int playerId) {
        this.inputs = inputs;
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Inputs() {
        inputs = new ArrayList<Integer>();
    }

    public List<Integer> getInputs() {
        return inputs;
    }

    public void addInput(int input) {
        inputs.add(input);
    }

    public void clear() {
        inputs.clear();
    }

}
