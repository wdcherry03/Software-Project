// Java program to illustrate Server side Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class udpServer {
	public Model model;
	DatagramSocket ds;
	byte[] receive;
	DatagramPacket DpReceive;
	public InetAddress ip;
	byte[] buf;
	public static udpServer server;
	int code = 0;
	public int i1;
	public int i2;

	// Costructor
	public udpServer(int port, Model m)
	{
		model = m;
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
			String input = "";
			String p1 = "";
			String p2 = "";
			i1 = 0;
			i2 = 0;

			// Recieve the data in byte buffer
			try {
				ds.receive(DpReceive);
			} 
			catch (IOException e) {
				System.out.println("ERROR receiving data");
				System.out.println(e.getMessage());
			}
			
			System.out.println("Received from Client: " + data(receive));

			// Split the received message (should be in int:int format, throws error otherwise)
			// (transmitting player):(hit player)
			input = data(receive).toString();
			if (input.contains(":")) {
				String parts[] = input.split(":");
				p1 = parts[0]; // ID of transmitting player
				p2 = parts[1]; // ID of hit player/base
				i1 = Integer.parseInt(p1);
				i2 = Integer.parseInt(p2);
			}
			else {
				throw new IllegalArgumentException("String " + input + " does not contain :");
			}
			
			// Transmit codes based on received data (odd red, even green)
			if (p2.equals("53")) {
				// Red base (code 53)
				System.out.println(p1 + " hit the red base\n");

				if (i1 % 2 == 0) {
					// Green player hit red base, +100 points & stylized B to left of codename
					System.out.println(p1 + " received points for hitting the enemy base\n");
					model.allPlayersList.get(model.checkPlayerListByID(i1)).hitBase();
				}
				send(p2);
			}
			else if (p2.equals("43")) { 
				// Green base (code 43)
				System.out.println(p1 + " hit the green base");

				if (i1 % 2 == 1) {
					// Red player hit green base, +100 points & stylized B to left of codename
					System.out.println(p1 + " received points for hitting the enemy base\n");
					model.allPlayersList.get(model.checkPlayerListByID(i1)).hitBase();
				}
				send(p2);
			}
			else if (i1 % 2 != i2 % 2) {
				// Player hit other team, transmit hit player id
				System.out.println(p1 + " hit an enemy player");
				model.allPlayersList.get(model.checkPlayerListByID(i1)).hitEnemyPlayer();
				send(p2);
				// +10 points for player
			}
			else {
				// Player hit same team, transmit own player id
				System.out.println(p1 + " hit a teammate");
				model.allPlayersList.get(model.checkPlayerListByID(i1)).hitTeamPlayer();
				send(p1);
				// -10 points for player
			}

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