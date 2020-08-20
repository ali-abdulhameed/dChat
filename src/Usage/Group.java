package Usage;

import java.util.ArrayList;

import javax.crypto.SecretKey;

import Views.ActivityGroupsPage;

/**
 * 
 * @author Ali Abdulhameed Class used to store a group that a user creates or
 *         join. It contains all group related functionalities.
 */
public class Group {

	/**
	 * Group ID. It's randomly generated when creating a new group. It is manually
	 * entered when joning a group.
	 */
	private String groupID;
	private String name;

	/**
	 * Unlike user-to-user messages, the same key is used for encrypting and
	 * descrypting all messages. Every message sent or received is encrypted with a
	 * random IV which gets sent with each message.
	 */
	private SecretKey secretKey;
	/**
	 * List containing messages between us and all users in the group.
	 */
	private Messages messages;

	public Group(String name, String groupID, SecretKey secretKey) {

		this.name = name;
		this.groupID = groupID;
		this.secretKey = secretKey;
		messages = new Messages();
	}

	public String getGroupID() {
		return groupID;
	}

	public String getName() {
		return name;
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * Sends a message to all users in the group.
	 * 
	 * @param message to be sent.
	 */
	public void sendMessage(String message) {
		synchronized (messages.getLock()) {
			Server.getInstance().sendGroupMessage(message, this, -1);
			addMessage(message, null);

		}
	}

	public void addMessage(String message, Client client) {
		messages.addMessage(client, message);
		ActivityGroupsPage.getInstance().updateActiveMessagesPanel(this);
	}

	public Object getMessagesLockObject() {
		return messages.getLock();
	}

	public Object[] getMessages() {
		return messages.getMessages();
	}

	public int getStartingMessageIndex() {
		return messages.getStartingMessageIndex();
	}

	private class Messages {

		private ArrayList<String> messages;
		private ArrayList<Client> clients;

		private Object lock;
		private int messageIndex;

		private Messages() {
			messages = new ArrayList<>();
			clients = new ArrayList<>();

			lock = new Object();
			messageIndex = 0;
		}

		private void addMessage(Client client, String message) {

			synchronized (lock) {
				try {

					messages.add(message);
					clients.add(client);
					lock.notifyAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		private int getStartingMessageIndex() {
			synchronized (lock) {
				int index = messageIndex;
				messageIndex = messages.size();
				return index;
			}
		}

		private Object[] getMessages() {
			synchronized (lock) {
				return new Object[] { messages, clients };
			}
		}

		private Object getLock() {

			return lock;

		}
	}

}
