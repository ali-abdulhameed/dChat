package Network;

import java.io.*;
import java.math.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import Usage.*;

/**
 * 
 * @author Ali Abdulhameed Class used for broadcasting, sending and receiving
 *         messages.
 *
 */
public class UDP {

	private static UDP instance;
	/**
	 * Stores the user network interfaces.
	 */
	private List<InetAddress> broadcastList;

	static {

		instance = new UDP();
	}

	private UDP() {
		broadcastList = getBroadcastList();
	}

	public static UDP getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param Message        to be broadcasted on networks the user is connected to.
	 * @param DatagramSocket for sending UDP packets.
	 * @param Port           used by receiver to receive packets.
	 */
	public void broadcast(String message, DatagramSocket socket, int port) {

		try {

			byte[] buffer = message.getBytes();
			for (InetAddress address : broadcastList) {
				socket.send(new DatagramPacket(buffer, buffer.length, address, port));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(byte[] message, DatagramSocket socket, InetAddress address, int port) {
		try {

			socket.send(new DatagramPacket(message, message.length, address, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return All network interfaces of the user.
	 */
	private List<InetAddress> getBroadcastList() {
		List<InetAddress> list = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || !networkInterface.isUp())
					continue;

				networkInterface.getInterfaceAddresses().stream().map(a -> a.getBroadcast()).filter(Objects::nonNull)
						.forEach(list::add);
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * It receives data, parse them, then calls the appropriate method for
	 * processing the data. There are three types of date being received, messages,
	 * group messages, and user broadcasted public key.
	 * 
	 * @param DatamgramSocket used to receive UDP packets. It's set to the default
	 *                        port 3000.
	 */
	public void receive(DatagramSocket socket) {

		try {

			byte[] buffer = new byte[65507];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String[] messageParams = new String(buffer).trim().split(" ", 4);

			if (messageParams[0].equals("publicKey")) {
				processPublicKeyReceivedThroughBroadcast(packet, new BigInteger(messageParams[2]),
						new BigInteger(messageParams[3]), messageParams[1]);
				return;
			}

			if (!Server.getInstance().getClients().containsKey(packet.getAddress()))
				return;

			if (messageParams[0].equals("group")) {
				System.out.println("cipher: " + new String(buffer));
				processGroupMessage(packet, messageParams[1],
						new IvParameterSpec(Base64.getDecoder().decode(messageParams[2])),
						Base64.getDecoder().decode(messageParams[3]));
				return;
			}

			processMessage(packet, buffer);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	/**
	 * 
	 * @param DatagramPacket received. It contains the sender's IP address which is
	 *                       used for identification purposes throughout the
	 *                       program. Data in it is already extracted in the
	 *                       receive() method.
	 * @param Data           extracted from received packet.
	 * @throws Exception thrown by bad decryption.
	 */
	private void processMessage(DatagramPacket packet, byte[] data) throws Exception {

		int i = data.length;
		while (i-- > 0 && data[i] == 0)
			;
		byte[] output = new byte[i + 1];
		byte[] AES_RSA_EncryptedKeyBytes = new byte[256];
		byte[] messageBytes = new byte[output.length - AES_RSA_EncryptedKeyBytes.length];

		System.arraycopy(data, 0, output, 0, i + 1);
		System.arraycopy(output, 0, AES_RSA_EncryptedKeyBytes, 0, 256);
		System.arraycopy(output, 256, messageBytes, 0, output.length - AES_RSA_EncryptedKeyBytes.length);

		byte[] AESKeyBytes = Network.Security.getInstance().decryptWithRSA(AES_RSA_EncryptedKeyBytes);
		SecretKey AESKey = new SecretKeySpec(AESKeyBytes, 0, AESKeyBytes.length, "AES");
		Client client = (Client) Server.getInstance().getClients().get(packet.getAddress());
		if (client == null)
			return;

		client.addMessage(new String(Network.Security.getInstance().decryptWithAES(messageBytes, AESKey)) + 1);
	}

	/**
	 * 
	 * @param Same    as above.
	 * @param groupID is the ID of the group the message being sent to. It's used
	 *                locate the appropriate group.
	 * @param Iv      is the AES IV used to encrypt the message with.
	 * @param data    contains the message contents.
	 */
	public void processGroupMessage(DatagramPacket packet, String groupID, IvParameterSpec Iv, byte[] data) {

		Group group = Server.getInstance().getGroups().get(groupID);
		if (group != null) {

			String message = new String(Security.getInstance().decryptWithAES(data, group.getSecretKey(), Iv));

			group.addMessage(message, Server.getInstance().getClients().get(packet.getAddress()));

		}
	}

	/**
	 * This is used to extracted public key parts received, then create a new public
	 * key and stores it in a client map that contains the sender's IP address and
	 * its public key.
	 * 
	 * @param Same           as above
	 * @param modulus        part of the public key.
	 * @param publicExponent part of the public key.
	 * @param name           contains the name of the user with the associated
	 *                       public key.
	 * @throws Exception thrown for incorrect public key being created. The modules
	 *                   and publicExponent don't go with each other.
	 */
	private void processPublicKeyReceivedThroughBroadcast(DatagramPacket packet, BigInteger modulus,
			BigInteger publicExponent, String name) throws Exception {

		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		Server.getInstance().addClient(packet.getAddress(), publicKey, name);
	}

}
