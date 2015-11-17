package com.mygdx.game.networking.test;

import com.mygdx.game.networking.DataHandler;
import com.mygdx.game.networking.NetworkAddress;
import com.mygdx.game.networking.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ServerTest implements DataHandler {

    private Server server;
    private int clientsCount;
    private boolean dataHasChanged;

    private Map<NetworkAddress, ClientData> clientsData;

    public ServerTest(int port) throws IOException {
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
        try {
            String s = "";
            for(ClientData cd: clientsData.values()) {
                s += new String(cd.data, "UTF-8");
            }
            return s.getBytes();
        } catch (Exception e) {
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
        ServerTest st = new ServerTest(1234);
        st.run();
    }

}
