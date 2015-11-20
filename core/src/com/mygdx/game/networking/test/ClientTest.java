package com.mygdx.game.networking.test;

import com.mygdx.game.networking.client.Client;

import java.io.IOException;
import java.util.Random;


public class ClientTest {

    static public void main(String[] args) throws IOException {
        int port = new Random().nextInt(60000) + 1;
        Client c = new Client(port);
        c.connect("localhost", 1234);
        while(true) {
            c.send((String.valueOf(port) + " ").getBytes());
            byte[] bytes = c.receive();
            if(bytes != null) {
                System.out.println(new String(bytes, "UTF-8"));
            } else {
//                System.out.println("Nothing");
            }
        }
    }

}
