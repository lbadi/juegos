package com.mygdx.game.networking.server;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.networking.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameServer implements DataHandler {

    private Server server;
    private int clientsCount;
    private boolean dataHasChanged;

    private Map<NetworkAddress, ClientData> clientsData;

    public GameServer(int port) throws IOException {
        server = new Server(port, this);
        clientsData = new HashMap<NetworkAddress, ClientData>();
        dataHasChanged = false;
    }

    public void run() throws IOException {
        System.out.println("Server started.");
        server.run();
    }

    @Override
    public void onDataReceived(byte[] data, String hostname, int port) {
        NetworkAddress c = new NetworkAddress(hostname, port);
        if(!clientsData.containsKey(c)) {
            clientsCount++;
            System.out.println("New client. Total clients: " + clientsCount);
        }
        dataHasChanged = true;
        clientsData.put(c, new ClientData(data));
    }

    @Override
    public byte[] onReadyToSend(String hostname, int port) {
        NetworkAddress c = new NetworkAddress(hostname, port);
        if(!clientsData.containsKey(c)) {
            clientsCount++;
        }
        if(dataHasChanged) {
            dataHasChanged = false;
            return computeState();
        }
        return null;
    }

    private byte[] computeState() {
        List<NetworkObject> objects = new ArrayList<NetworkObject>();
        Vector3 position = new Vector3(0,-1f, -4);
        Vector3 rotation = new Vector3(-(float) Math.PI / 12, (float) Math.PI, 0);
        Vector3 scale = new Vector3(1, 1, 1);
        NetworkObject object = new NetworkObject(1, position, rotation, scale);
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
