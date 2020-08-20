package Usage;

import java.net.*;
import java.security.*;
import java.security.interfaces.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Network.*;
import Network.Security;
import Utilities.TrueIDGenerator;
import Views.ActivityGroupsPage;
import Views.ActivityPage;
import Views.Dashboard;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class Server {

	private static final Server instance;
	private volatile String name;

	// Stores users and their associated Client object.
	private Map<InetAddress, Client> clients;
	// Stores group IDs along their associated Group object.
	private Map<String, Group> groups;
	// Stores the desc of the listener along the UDP packet listener Object.
	private Map<String, PacketListener> activePorts;
	// Stores the desc of the broadcast along the UDP broadcast Object.
	private Map<String, Broadcast> activeBroadcasts;
	// Socket used for sending and receiving UDP packets.
	private DatagramSocket socket;
	private int DEFAULT_PORT = 3000;
	// Number of users allowed to communicate with. 0 Stands for infinite.
	private int maximumNumberOfUsersAllowed = 0;

	static {
		instance = new Server();
	}

	private Server() {

		clients = Collections.synchronizedMap(new HashMap<>());
		groups = Collections.synchronizedMap(new HashMap<>());
		activePorts = Collections.synchronizedMap(new HashMap<>());
		activeBroadcasts = Collections.synchronizedMap(new HashMap<>());

		try {

			name = "ME";
			socket = new DatagramSocket(DEFAULT_PORT);
			socket.setBroadcast(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Server getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param desc      information about the message being broadcasted.
	 * @param message   being broadcasted.
	 * @param port      used by receiver to listen packets.
	 * @param frequency how often should a message be broadcasted.
	 */
	public void broadcastMessage(String desc, String message, int port, long frequency) {

		if (activeBroadcasts.containsKey(desc)) {
			Broadcast broadcast = activeBroadcasts.get(desc);
			broadcast.setMessage(message);
			broadcast.setPort(port < 0 ? DEFAULT_PORT : port);
			broadcast.setFrequency(frequency);
		} else {
			activeBroadcasts.put(desc, new Broadcast(message, port < 0 ? DEFAULT_PORT : port, frequency));
			Dashboard.getInstance().updateNumberOfActiveBroadcasts();
		}

	}

	/**
	 * used to broadcasts our public key to other users on the network.
	 * 
	 * @param port      used by receiver to listen packets.
	 * @param frequency how often should a public key be broadcasted.
	 */
	public void broadcastPublicKey(int port, long frequency) {

		if (!activeBroadcasts.containsKey("publicKey")) {
			RSAPublicKey publicKey = (RSAPublicKey) (Network.Security.getInstance().getPublicKey());

			activeBroadcasts.put("publicKey",
					new Broadcast("publicKey" + " " + (name.equals("ME") ? "default" : name) + " "
							+ publicKey.getModulus() + " " + publicKey.getPublicExponent(),
							port < 0 ? DEFAULT_PORT : port, frequency));
		}
	}

	/**
	 * Update our name which gets sent to all users every time our public key is
	 * broadcasted.
	 */
	public void updatePublicKeyBroadcastMessage() {
		Broadcast pkb = activeBroadcasts.get("publicKey");
		String[] message = pkb.getMessage().split(" ");
		if (pkb != null) {
			message[1] = name;
			pkb.setMessage(String.join(" ", message));
		}
	}

	/**
	 * Sends a message to a user using its IP address which is store in its Client
	 * object.
	 * 
	 * @param message being sent to user
	 * @param client  user Client object
	 * @param port    used by user to listen to messages packets.
	 */
	public void sendMessage(String message, Client client, int port) {
		try {
			Security security = Network.Security.getInstance();
			SecretKey key = security.generateAESSecretKey();
			byte[] encryptedMessage = security.encryptWithAES(message, key);
			byte[] encryptedKey = security.encryptWithRSA(Base64.getEncoder().encodeToString(key.getEncoded()),
					client.getPublicKey());
			byte[] combined = new byte[encryptedKey.length + encryptedMessage.length];

			System.arraycopy(encryptedKey, 0, combined, 0, encryptedKey.length);
			System.arraycopy(encryptedMessage, 0, combined, encryptedKey.length, encryptedMessage.length);
			UDP.getInstance().sendMessage(combined, socket, client.get_IP_ADDRESS(), port < 0 ? DEFAULT_PORT : port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to all users in a group. The difference between it and in
	 * user-to-user messages is that a group message is broadcasted and users that
	 * have the correct AES encryption key associated with the group will be able to
	 * decrypt the message.
	 * 
	 * @param message being sent.
	 * @param group   the group the message is being sent to.
	 * @param port    used by groups users to listen to messages packets.
	 */
	public void sendGroupMessage(String message, Group group, int port) {
		Security security = Network.Security.getInstance();
		SecretKey key = group.getSecretKey();
		IvParameterSpec Iv = security.generateAESIv();
		String encryptedMessage = Base64.getEncoder().encodeToString(security.encryptWithAES(message, key, Iv));
		String IvString = Base64.getEncoder().encodeToString(Iv.getIV());
		UDP.getInstance().broadcast("group " + group.getGroupID() + " " + IvString + " " + encryptedMessage, socket,
				port < 0 ? DEFAULT_PORT : port);

	}

	/**
	 * Creates new group. Generates random group ID and AES 128bits key and store
	 * them in the group object.
	 * 
	 * @param name group name.
	 * @return group newly generated ID and AES secret key,
	 */
	public String createGroup(String name) {
		String groupID = TrueIDGenerator.getInstance().generateID(12);
		SecretKey secretKey = Security.getInstance().generateAESSecretKey();
		Group group = new Group(name, groupID, secretKey);
		groups.put(groupID, group);

		ActivityGroupsPage.getInstance().updateClientList(group);
		Dashboard.getInstance().updateNumberOfGroups();
		return String.format(
				"Users will need to know your group ID%nand secret key" + " in order for them to join.%n%n"
						+ "Group ID: %s%nSecret key: %s",
				groupID, Base64.getEncoder().encodeToString(secretKey.getEncoded()));

	}

	/**
	 * Joins a group. It requires its ID and secret key to be entered.
	 * 
	 * @param name      of the group. Can be anything.
	 * @param groupID   group ID. It's the ID that is generated when the group was
	 *                  first created.
	 * @param secretKey the secret key that was generated when the group was first
	 *                  created.
	 * @return status of joining the group attempt, error or success.
	 */
	public String joinGroup(String name, String groupID, String secretKey) {

		if (!groups.containsKey(groupID)) {
			String textValidityError = "";
			boolean valid = true;
			if (groupID.replaceAll("\\s+", "").length() != groupID.length()) {
				textValidityError = "Error: group ID must not contain spaces.<br>";
				valid = false;
			}
			if (secretKey.replaceAll("\\s+", "").length() != secretKey.length()) {
				textValidityError += "Error: secret key must not contain spaces.";
				valid = false;
			}

			if (!valid)
				return "<html>" + textValidityError + "</html>";

			byte[] skBytes;
			try {
				skBytes = Base64.getDecoder().decode(secretKey);
			} catch (Exception e) {
				return "Error: illegal key characters";
			}

			if (skBytes.length * 8 < 128) {
				return "Error: wrong key size.";

			}
			try {
				SecretKey sk = new SecretKeySpec(skBytes, 0, skBytes.length, "AES");
				Group group = new Group(name, groupID, sk);
				groups.put(groupID, group);
				ActivityGroupsPage.getInstance().updateClientList(group);
				Dashboard.getInstance().updateNumberOfGroups();
			} catch (Exception e) {
				return "Error: illegal key.";
			}

			return "Group has been joined successfully.";
		} else {
			return "Error: group already exists.";
		}

	}

	/**
	 * Starts the server.
	 */
	public static void start() {
		Server server = Server.getInstance();
		int port = -1;
		int frequency = 5000;
		server.broadcastPublicKey(port, frequency);
		server.listenForUDP("general", port);
	}

	public void listenForUDP(String desc, int port) {
		activePorts.put(desc, new PacketListener(port));
	}

	public Map<String, Broadcast> getActiveBroadcasts() {
		return activeBroadcasts;
	}

	public Map<String, PacketListener> getActivePorts() {
		return activePorts;
	}

	public synchronized Map<InetAddress, Client> getClients() {
		return clients;
	}

	public Map<String, Group> getGroups() {
		return groups;
	}

	public String getName() {
		return name;
	}

	public int getMaximumNumberOfUsersAllowed() {
		return maximumNumberOfUsersAllowed;
	}

	public void setMaximumNumberOfUsersAllowed(int max) {
		maximumNumberOfUsersAllowed = max;
	}

	/**
	 * Adds a new user (client) to the server.
	 * 
	 * @param address   IP address of the user.
	 * @param publicKey of the user.
	 * @param name      of the user.
	 */
	public synchronized void addClient(InetAddress address, PublicKey publicKey, String name) {

		Client client;
		if (!Network.Security.getInstance().getPublicKey().equals(publicKey) && !clients.containsKey(address)
				&& (maximumNumberOfUsersAllowed == 0 || (clients.size() < maximumNumberOfUsersAllowed))) {
			client = new Client(publicKey, address);
			if (!name.equals("default"))
				client.setName(name);
			clients.put(client.get_IP_ADDRESS(), client);
			ActivityPage.getInstance().updateClientList(client);
			Dashboard.getInstance().updateNumberOfFriends();
			return;
		}

		if (clients.containsKey(address)) {
			client = clients.get(address);
			if (!client.getPublicKey().equals(publicKey)) {
				client.setPublicKey(publicKey);
			}
			if (!name.equals("default") && !client.getName().equals(name)) {
				client.setName(name);
			}
		}
	}

	/**
	 * Changes our name.
	 * 
	 * @param name our name
	 */
	public void setName(String name) {
		if (name.equals("ME"))
			return;
		this.name = name;
		updatePublicKeyBroadcastMessage();
	}

	/**
	 * 
	 * @author Ali Abdulhameed Class used for broadcasting a single message.
	 *
	 */
	public class Broadcast {

		private int port;
		private String message;
		private long frequency;
		private boolean running = true;

		private Broadcast(String message, int port, long frequency) {
			this.port = port;
			this.message = message;
			this.frequency = frequency;
			if (frequency == 0)
				running = false;

			try {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {

							do {

								UDP.getInstance().broadcast(getMessage(), socket, getPort());
								Thread.sleep(frequency);

							} while (running);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public int getPort() {
			return port;
		}

		public synchronized String getMessage() {
			return message;
		}

		public synchronized long getFrequency() {
			return frequency;
		}

		public synchronized void setMessage(String message) {
			this.message = message;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public void setFrequency(long frequency) {
			this.frequency = frequency;
		}

		public void kill() {
			running = false;
		}

	}

	/**
	 * 
	 * @author Ali Abdulhameed Class used for listening on a specific port.
	 *
	 */
	private class PacketListener {

		private int port;
		private boolean running = true;

		private PacketListener(int port) {

			this.port = port < 0 ? DEFAULT_PORT : port;

			try {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {

							while (running) {

								UDP.getInstance().receive(socket);

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int getPort() {
			return port;
		}

		public void kill() {
			running = false;
		}

	}
}
