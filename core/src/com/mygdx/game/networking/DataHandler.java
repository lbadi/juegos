package com.mygdx.game.networking;


import java.io.UnsupportedEncodingException;

public interface DataHandler {

    void onDataReceived(byte[] data, String hostname, int port);
    byte[] onReadyToSend(String hostname, int port);
}
