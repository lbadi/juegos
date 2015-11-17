package com.mygdx.game.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;

public class Server {

    private static final int TIMEOUT = 3000; // Wait timeout (milliseconds) 
    private static final int BUFFER_SIZE = 1024;

    private Selector selector;
    private DataHandler dataHandler;

    private class ClientRecord {
        public InetSocketAddress clientAddress;
        public ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        public ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public Server(int port, DataHandler dataHandler) throws IOException {
        this.dataHandler = dataHandler;
        // Create a selector to multiplex client connections.
        selector = Selector.open();
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_READ, new ClientRecord());
    }

    public void run() throws IOException {
        while (true) { // Run forever, receiving and echoing datagrams
            // Wait for task or until timeout expires
            if (selector.select(TIMEOUT) == 0) {
                System.out.print(".");
                continue;
            }

            // Get iterator on set of keys with I/O to process
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next(); // Key is bit mask

                // Client socket channel has pending data?
                if (key.isReadable()) {
                    handleRead(key);
                }

                // Client socket channel is available for writing and
                // key is valid (i.e., channel not closed).
                if (key.isValid() && key.isWritable()) {
                    handleWrite(key);
                }
                keyIterator.remove();
            }
        }
    }

    public void handleRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ClientRecord client = (ClientRecord) key.attachment();
        client.readBuffer.clear(); // Prepare buffer for receiving
        client.clientAddress = (InetSocketAddress) channel.receive(client.readBuffer);
        if (client.clientAddress != null) {  // Did we receive something?
            dataHandler.onDataReceived(Arrays.copyOfRange(client.readBuffer.array(), 0, client.readBuffer.position()),
                    client.clientAddress.getHostName(), client.clientAddress.getPort());
            // Register write with the selector
            key.interestOps(SelectionKey.OP_WRITE);
        }

    }

    public void handleWrite(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ClientRecord client = (ClientRecord) key.attachment();
        if(client.clientAddress != null) {
            byte[] data = dataHandler.onReadyToSend(client.clientAddress.getHostName(), client.clientAddress.getPort());
            if (data != null) {
                client.writeBuffer.clear();
                client.writeBuffer.put(data);
                client.writeBuffer.flip();
                channel.send(client.writeBuffer, client.clientAddress);
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

}