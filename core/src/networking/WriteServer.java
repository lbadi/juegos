package networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

class WriteServer {
	
  public static void main(String args[]) throws Exception {
    int clientPort = 2000;

    int buffer_size = 1024;

    byte buffer[] = new byte[buffer_size];
    DatagramSocket ds = new DatagramSocket(clientPort);
    while (true) {
      DatagramPacket p = new DatagramPacket(buffer, buffer.length);
      ds.receive(p);
      System.out.println("HOLA");
      System.out.println(new String(p.getData(), 0, p.getLength()));
    }
  }
}