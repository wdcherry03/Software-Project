// Java program to illustrate Server side Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class udpServer {
	DatagramSocket ds;
	byte[] receive;
	DatagramPacket DpReceive;
	public InetAddress ip;
	byte[] buf;
	public static udpServer server;
	int code = 0;

	public static void main(String[] args) {
		server = new udpServer(7501);
		server.run();
	}

	// Costructor
	public udpServer(int port)
	{
		try {
			// Create a socket to listen at given port
			ds = new DatagramSocket(port);
			receive = new byte[65535];
			DpReceive = null;

			// ds.setSoTimeout(1);
		}
		catch (SocketException e) {
			System.out.println("ERROR creating socket");
			System.out.println(e.getMessage());
		}

		try {
			//ip = InetAddress.getLocalHost();
			// ip = InetAddress.getByName("192.168.1.100"); // Address given in slides
			ip = InetAddress.getByName("127.0.0.1"); // Default IP address in traffic generator
		}
		catch (UnknownHostException e) {
			System.out.println("ERROR connecting to host");
			System.out.println(e.getMessage());
		}
		buf = null;

		//System.out.println("server constructor");
	}

	public void run() {
		while (true) {
			// Create a DatgramPacket to receive the data.
			DpReceive = new DatagramPacket(receive, receive.length);

			// Recieve the data in byte buffer
			try {
				ds.receive(DpReceive);
			} 
			catch (IOException e) {
				System.out.println("ERROR receiving data");
				System.out.println(e.getMessage());
			}
			
			System.out.println("Received from Client: " + data(receive));

			// Transmit codes based on received data
			// TODO
			//server.send("12");

			// if (data(receive).toString().equals("bye"))
			// {
			// 	System.out.println("Client sent bye.....EXITING");
			// 	break;
			// }

			// Clear the buffer after every message.
			receive = new byte[65535];
		}
	}

	public void send(String Input) {
		String input = Input;

		// convert the String input into the byte array.
		buf = input.getBytes();

		// Step 2 : Create the datagramPacket for sending the data.
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 7500);

		try {
			// Step 3 : invoke the send call to actually send the data.
			System.out.println("Sending to Client: " + input);
			ds.send(DpSend);
		}
		catch (IOException e) {
			System.out.println("ERROR sending data");
			System.out.println(e.getMessage());
		}
	}	

	// A utility method to convert the byte array data into a string representation.
	public static StringBuilder data(byte[] a) {
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			ret.append((char) a[i]);
			i++;
		}
		return ret;
	}

}