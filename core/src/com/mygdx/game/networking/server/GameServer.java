package com.mygdx.game.networking.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.networking.*;

import java.io.*;
import java.util.*;


public class GameServer implements DataHandler {

    private Server server;
    private int clientsCount;
    private GameServerSimulation simulation;

    private Map<NetworkAddress, Inputs> clientInputs;

    public GameServer(int port) throws IOException {
        server = new Server(port, this);
        clientInputs = new HashMap<NetworkAddress, Inputs>();
        simulation = new GameServerSimulation();
    }

    public void run() throws IOException {
        System.out.println("Server started.");
        new Thread(simulation).run();
        server.run();
    }

    @Override
    public void onDataReceived(byte[] data, String hostname, int port) {
        NetworkAddress c = new NetworkAddress(hostname, port);
        if(!clientInputs.containsKey(c)) {
            clientsCount++;
            System.out.println("New client. Total clients: " + clientsCount);
        }
        try {
            Inputs inputs = (Inputs) Serializer.deserialize(data);
            simulation.enqueue(inputs);
            clientInputs.put(c, inputs);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public byte[] onReadyToSend(String hostname, int port) {
        return computeState();
    }

    private byte[] computeState() {
        GameState gs = simulation.getGameState();
        try {
            return Serializer.serialize(gs);
        } catch (IOException e) {
            return null;
        }
    }

    static public void main(String[] args) throws IOException {
        GameServer st = new GameServer(9001);
        st.run();
    }

}
