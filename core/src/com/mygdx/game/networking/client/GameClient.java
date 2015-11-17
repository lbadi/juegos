package com.mygdx.game.networking.client;

import com.mygdx.game.networking.GameState;
import com.mygdx.game.networking.NetworkAddress;
import com.mygdx.game.networking.Serializer;

import java.io.IOException;

/**
 * Created by lucas on 11/17/15.
 */
public class GameClient {

    private Client client;

    public GameClient(int port) throws IOException {
        client = new Client(port);
    }

    public void connect(NetworkAddress server) throws IOException {
        client.connect(server.getHostname(), server.getPort());
    }

    public GameState updateState(NetworkAddress server) {
        try {
            byte[] data = client.receive();
            if(data != null) {
                return (GameState) Serializer.deserialize(data);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
