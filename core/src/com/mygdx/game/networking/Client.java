package com.mygdx.game.networking;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;


public class Client {

    private static final int BUFFER_SIZE = 1024;

    private DatagramChannel channel;
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;
    private SocketAddress remote;

    public Client(int port) throws IOException {
        InetSocketAddress isa = new InetSocketAddress(port);
        channel = DatagramChannel.open();
        channel.socket().bind(isa);
        channel.configureBlocking(false);
        readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    private void connect(String hostname, int port) throws IOException {
        remote = new InetSocketAddress(InetAddress.getByName(hostname), port);
        channel.connect(remote);
    }

    private void send(byte[] bytes) throws IOException {
        writeBuffer.clear();
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.send(writeBuffer, remote);
    }

    private byte[] receive() throws IOException {
        readBuffer.clear();
        if(channel.receive(readBuffer) != null) {
            return Arrays.copyOfRange(readBuffer.array(), 0, readBuffer.position());
        }
        return null;
    }

    static public void main(String[] args) throws IOException {
        Client c = new Client(3452);
        c.connect("localhost", 1234);
        for(int i = 0; i < 100; i ++) {
            c.send("Hello UDP!".getBytes());
            byte[] bytes = c.receive();
            if(bytes != null) {
                System.out.println(new String(bytes, "UTF-8"));
            } else {
                System.out.println("Nothing");
            }
        }
    }
}
