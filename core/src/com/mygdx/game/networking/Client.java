package com.mygdx.game.networking;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.stream.IntStream;


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

    public void connect(String hostname, int port) throws IOException {
        remote = new InetSocketAddress(InetAddress.getByName(hostname), port);
        channel.connect(remote);
    }

    public void send(byte[] bytes) throws IOException {
        writeBuffer.clear();
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.send(writeBuffer, remote);
    }

    public byte[] receive() throws IOException {
        readBuffer.clear();
        if(channel.receive(readBuffer) != null) {
            return Arrays.copyOfRange(readBuffer.array(), 0, readBuffer.position());
        }
        return null;
    }
}
