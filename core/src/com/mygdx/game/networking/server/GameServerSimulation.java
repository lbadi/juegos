package com.mygdx.game.networking.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.networking.GameState;
import com.mygdx.game.networking.Input;
import com.mygdx.game.networking.Inputs;
import com.mygdx.game.networking.NetworkObject;
import com.mygdx.game.objects.GenericObject;

import java.util.*;

/**
 * Created by lucas on 11/19/15.
 */
public class GameServerSimulation implements Runnable {

    private Simulation simulation;

    private Queue<Inputs> inputs;

    private Map<Integer, GenericObject> players;

    private GameState gameState;


    public GameServerSimulation() {
        simulation = new Simulation();
        inputs = new LinkedList<Inputs>();
        players = new HashMap<Integer, GenericObject>();
    }

    @Override
    public void run() {
        new Timer().schedule(simulation, 0, 15);
    }

    public void enqueue(Inputs inputs) {
        this.inputs.offer(inputs);
    }

    public GameState getGameState() {
        return gameState;
    }

    private static long framesProcessed;

    private class Simulation extends TimerTask {

        @Override
        public void run() {
            Inputs currentInputs = inputs.poll();
            while(currentInputs != null) {
                applyInputs(currentInputs);
                currentInputs = inputs.poll();
            }
            List<NetworkObject> objects = new ArrayList<NetworkObject>();
            for(GenericObject player: players.values()) {
                objects.add(convertToNetworkObject(player));
            }
            gameState = new GameState(objects);
            framesProcessed++;
            if(framesProcessed % 60 == 0) {
                System.out.println("Frames procesados: " + framesProcessed);
            }
        }

        private void applyInputs(Inputs inputs) {
            if(inputs == null)
                return;
            GenericObject player;
            if(!players.containsKey(inputs.getPlayerId())) {
                player = new GenericObject();
                player.setPosition(new Vector3(0, 5, 4));
                player.setRotation(new Vector3(-(float) Math.PI / 4, 0, 0));
                player.setScaleVector(1, 1, 1);
                players.put(inputs.getPlayerId(), player);
            } else {
                player = players.get(inputs.getPlayerId());
            }
            boolean moveForward = false;
            boolean moveBackward = false;
            for(Input input: inputs.getInputs()) {
                switch (input) {
                    case MOVE_FORWARD:
                        moveForward = true;
                        break;
                    case MOVE_BACKWARD:
                        moveBackward = true;
                        break;
                }
            }
            if(moveForward) {
                player.setFowardSpeed(-0.1f);
            } else if (moveBackward){
                player.setFowardSpeed(0.1f);
            } else {
                player.setFowardSpeed(0);
            }
            player.betaMove();
        }

        private NetworkObject convertToNetworkObject(GenericObject object) {
            return new NetworkObject(object.getId(),
                    object.getPosition(),
                    object.getRotation(),
                    object.getScaleVector());
        }
    }

}
