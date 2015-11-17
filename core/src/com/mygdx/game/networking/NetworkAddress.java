package com.mygdx.game.networking;


public class NetworkAddress {

    private String hostname;
    private int port;

    public NetworkAddress(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkAddress client = (NetworkAddress) o;

        if (port != client.port) return false;
        return !(hostname != null ? !hostname.equals(client.hostname) : client.hostname != null);

    }

    @Override
    public int hashCode() {
        int result = hostname != null ? hostname.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

}
