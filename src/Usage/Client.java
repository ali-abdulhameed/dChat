package Usage;

import java.net.*;
import java.security.*;
import java.util.*;

import Views.ActivityPage;

/**
 * 
 * @author Ali Abdulhameed Class used to store the public key and IP address of
 *         every user that has broadcasted its public key on the network. It has
 *         all user related functionalities such as sending messages.
 *
 */
public class Client {

	// Default user name
	private String name = "Anonymous";
	private PublicKey publicKey;
	private final InetAddress IP_ADDRESS;
	// Messages sent or received to/by the user.
	private Messages messages;

	public Client(PublicKey publicKey, InetAddress IP_ADDRESS) {
		this.publicKey = publicKey;
		this.IP_ADDRESS = IP_ADDRESS;
		messages = new Messages();

	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public InetAddress get_IP_ADDRESS() {
		return IP_ADDRESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		ActivityPage.getInstance().changeClientName(this, name);

	}

	/**
	 * Method used to send a message to a user. The message gets added to the
	 * messages list associated with the user, a 0 is added to the end of it to
	 * indicate that it's sent by us. When a message is received, a 1 is added to
	 * its end to indicated that its sent from a user (client).
	 * 
	 * @param message sent to user.
	 */
	public void sendMessage(String message) {
		synchronized (messages.getLock()) {
			Server.getInstance().sendMessage(message, this, -1);
			addMessage(message + 0);

		}
	}

	/**
	 * 
	 * @param message to be added to a list of messages that contains messages sent
	 *                and received by/to the user.
	 */
	public void addMessage(String message) {
		messages.addMessage(message);
		ActivityPage.getInstance().updateActiveMessagesPanel(this);
	}

	public void printMessages() {
		messages.printMessages();
		;
	}

	public Object getMessagesLockObject() {
		return messages.getLock();
	}

	public ArrayList<String> getMessages() {
		return messages.getMessages();
	}

	public Iterator<String> getMessagesIterator() {
		return messages.getIterator();
	}

	/**
	 * 
	 * @return index of message in the messages list that was added to the JPanel
	 *         associated with the user showing the messages exchange between it and
	 *         us. This is to avoid the need of printing all messages every time
	 *         messages between us and the user is requested.
	 */
	public int getStartingMessageIndex() {
		return messages.getStartingMessageIndex();
	}

	private class Messages {

		// Containing messages between us and user.
		private ArrayList<String> messages;
		private Iterator<String> iterator;
		private Object lock;
		private int messageIndex;

		private Messages() {
			messages = new ArrayList<>();
			iterator = messages.iterator();
			lock = new Object();
			messageIndex = 0;
		}

		private void addMessage(String message) {

			synchronized (lock) {
				try {

					messages.add(message);
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

		private ArrayList<String> getMessages() {
			synchronized (lock) {
				return messages;
			}
		}

		private Iterator<String> getIterator() {
			synchronized (lock) {
				return iterator;
			}
		}

		private void printMessages() {
			synchronized (lock) {
				while (iterator.hasNext()) {
					System.out.println(iterator.next());
				}

			}
		}

		private Object getLock() {

			return lock;

		}
	}

}
