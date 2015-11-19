package com.mygdx.game.networking.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.networking.*;

import java.io.*;
import java.util.*;


public class GameServer implements DataHandler {

    private Server server;
    private int clientsCount;
    private boolean dataHasChanged;

    private Map<NetworkAddress, Inputs> clientInputs;

    public GameServer(int port) throws IOException {
        server = new Server(port, this);
        clientInputs = new HashMap<NetworkAddress, Inputs>();
        dataHasChanged = false;
    }

    public void run() throws IOException {
        System.out.println("Server started.");
        server.run();
    }

    @Override
    public void onDataReceived(byte[] data, String hostname, int port) {
        NetworkAddress c = new NetworkAddress(hostname, port);
        if(!clientInputs.containsKey(c)) {
            clientsCount++;
            System.out.println("New client. Total clients: " + clientsCount);
        }
        dataHasChanged = true;
        try {
            Inputs inputs = (Inputs) Serializer.deserialize(data);
            for(Input i: inputs.getInputs()) {
                System.out.println(i);
            }
            clientInputs.put(c, inputs);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public byte[] onReadyToSend(String hostname, int port) {
        return computeState();
    }

    Vector3 position = new Vector3(0, 5, 4);
    Vector3 rotation = new Vector3(-(float) Math.PI / 4, 0, 0);
    Vector3 scale = new Vector3(1, 1, 1);

    private boolean movingForward;
    private boolean movingBackward;

    private byte[] computeState() {

        for(Inputs is: clientInputs.values()) {
            for(Input i: is.getInputs()) {
                switch (i) {
                    case MOVE_FORWARD_PRESSED:
                        movingForward = true;
                        position.add(new Vector3(0, 0, -0.05f));
                        break;
                    case MOVE_FORWARD_RELEASED:
                        movingForward = false;
                        break;
                    case MOVE_BACKWARD_PRESSED:
                        movingBackward = true;
                        position.add(new Vector3(0, 0, 0.05f));
                        break;
                    case MOVE_BACKWARD_RELEASED:
                        movingBackward = false;
                        break;
                }
            }
        }

        if(movingForward) {
            position.add(new Vector3(0, 0, -0.05f));
        } else if(movingBackward) {
            position.add(new Vector3(0, 0, 0.05f));
        }

        List<NetworkObject> objects = new ArrayList<NetworkObject>();
        NetworkObject object = new NetworkObject(15, position, rotation, scale);
        objects.add(object);
        GameState gs = new GameState(objects);
        try {
            return Serializer.serialize(gs);
        } catch (IOException e) {
            return null;
        }

    }


    private class ClientData {
        public byte[] data;

        public ClientData(byte[] data) {
            this.data = data;
        }

    }

    static public void main(String[] args) throws IOException {
        GameServer st = new GameServer(1234);
        st.run();
    }

}
