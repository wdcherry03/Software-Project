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

	// Costructor
	public udpServer() throws SocketException
	{
		try {
			// Step 1 : Create a socket to listen at port 7501
			ds = new DatagramSocket(7501);
			receive = new byte[65535];
			
			DpReceive = null;
		} 
		catch (SocketException e) {
			System.out.println("ERROR creating socket");
			System.out.println(e.getMessage());
		}

		try {
			ip = InetAddress.getLocalHost();
			//InetAddress ip = InetAddress.getByName("192.168.1.100"); // Address given in slides
		}
		catch (UnknownHostException e) {
			System.out.println("ERROR connecting to host");
			System.out.println(e.getMessage());
		}
		buf = null;

		System.out.println("server constructor");
	}

	// Update function, runs every frame
	public int update() {
		// Step 2 : create a DatgramPacket to receive the data.
		DpReceive = new DatagramPacket(receive, receive.length);
		
		if (DpReceive != null) {
			//System.out.println("update");
			try {
				// Step 3 : recieve the data in byte buffer.
				ds.receive(DpReceive);
			} catch (IOException e) {
				System.out.println("ERROR receiving data");
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Client: " + data(receive));

		// Exit the server if the client sends "bye"
		if (data(receive).toString().equals("bye")) {
			System.out.println("Client sent bye.....EXITING");
			return 1; // Returns 1 when exiting
		}

		// Clear the buffer after every message.
		receive = new byte[65535];
		return 0;
	}

	public void Recieve() {
		// Create a DatgramPacket to receive the data.
		DpReceive = new DatagramPacket(receive, receive.length);

		if (DpReceive != null) {
			try {
				// Recieve the data in byte buffer
				ds.receive(DpReceive);
			} catch (IOException e) {
				System.out.println("ERROR receiving data");
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Client: " + data(receive));

		// Exit the server if the client sends "bye"
		// if (data(receive).toString().equals("bye")) {
		// 	System.out.println("Client sent bye.....EXITING");
		// }

		// Clear the buffer after every message.
		receive = new byte[65535];
	}

	public void Send(String Input) {
		String input = Input;

		// convert the String input into the byte array.
		buf = input.getBytes();

		// Step 2 : Create the datagramPacket for sending the data.
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 7500);

		try {
			// Step 3 : invoke the send call to actually send the data.
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

/*
	public static void main(String[] args) throws IOException
	{
		// Step 1 : Create a socket to listen at port 7501
		DatagramSocket ds = new DatagramSocket(7501);
		byte[] receive = new byte[65535];

		DatagramPacket DpReceive = null;
		while (true)
		{

			// Step 2 : create a DatgramPacket to receive the data.
			DpReceive = new DatagramPacket(receive, receive.length);

			// Step 3 : recieve the data in byte buffer.
			ds.receive(DpReceive);

			System.out.println("Client:-" + data(receive));

			// Exit the server if the client sends "bye"
			if (data(receive).toString().equals("bye"))
			{
				System.out.println("Client sent bye.....EXITING");
				break;
			}

			// Clear the buffer after every message.
			receive = new byte[65535];
		}
	}
*/