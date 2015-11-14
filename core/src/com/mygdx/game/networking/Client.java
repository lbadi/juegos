package com.mygdx.game.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Client {
    static int BUF_SZ = 1024;

    private Selector selector;
    private DatagramChannel channel;


    class Connection {
        ByteBuffer inputs;
        ByteBuffer state;
        SocketAddress sa;

        public Connection() {
            inputs = ByteBuffer.allocate(BUF_SZ);
        }
    }

    private void connect(String hostname, int port) throws IOException {
        selector = Selector.open();
        channel = DatagramChannel.open();
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getByName(hostname), port);
        channel.socket().bind(isa);
        channel.configureBlocking(false);
        SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        clientKey.attach(new Connection());
    }

    private void sendInputs() throws ClosedChannelException {
        try {
            selector.select();
            Iterator selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                try {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                } catch (IOException e) {
                    System.err.println("glitch, continuing... " +(e.getMessage()!=null?e.getMessage():""));
                }
            }
        } catch (IOException e) {
            System.err.println("glitch, continuing... " +(e.getMessage()!=null?e.getMessage():""));
        }
    }

    private void read(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel)key.channel();
        Connection con = (Connection)key.attachment();
        con.sa = chan.receive(con.state);
        System.out.println(new String(con.state.array(), "UTF-8"));
    }

    private void write(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel)key.channel();
        Connection c = (Connection) key.attachment();
        c.inputs.put("Hola".getBytes(Charset.forName("UTF-8")));
        chan.send(c.inputs, c.sa);
    }

    static public void main(String[] args) throws IOException {
        Client c = new Client();
        c.connect("192.168.0.100", 1234);
        c.sendInputs();
    }
}
