package Network;

import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * 
 * @author Ali Abdulhameed Singleton class used to encrypt and decrypt messages.
 *         It stores the user RSA key pair (public and private).
 *
 */
public class Security {

	private static final Security instance;
	private KeyPair RSAKeyPair;

	static {
		instance = new Security();
	}

	private Security() {
		try {
			RSAKeyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Security getInstance() {
		return instance;
	}

	/**
	 * 
	 * @return a random AES 128bits key.
	 */
	public SecretKey generateAESSecretKey() {
		SecretKey key = null;
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(128);
			key = generator.generateKey();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * 
	 * @return a random AES 128bits IV.
	 */
	public IvParameterSpec generateAESIv() {
		SecureRandom randomSecureRandom = new SecureRandom();
		byte[] iv = new byte[16];
		randomSecureRandom.nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public PublicKey getPublicKey() {
		return RSAKeyPair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return RSAKeyPair.getPrivate();
	}

	/**
	 * This will be used to encrypt the AES Key that was used to encrypt the message
	 * being sent to the user.
	 * 
	 * @param Message   to be encrypted
	 * @param Recipient public key (user the message being sent to)
	 * @return Encrypted message.
	 */
	public byte[] encryptWithRSA(String message, PublicKey publicKey) {

		byte[] cipherText = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"),
					PSource.PSpecified.DEFAULT);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParams);
			cipherText = cipher.doFinal(message.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;

	}

	/**
	 * This will be used to encrypt the message content with a random AES 128bits
	 * key. For user-to-user messages, the same key will be used as IV since a
	 * random key is generated from each new message thereby security will not be
	 * compromised.
	 * 
	 * @param Message to be encrypted
	 * @param Key     to encrypt the message with. It's randomly generated.
	 * @return Encrypted message.
	 */
	public byte[] encryptWithAES(String message, SecretKey key) {
		return encryptWithAES(message, key, null);
	}

	/**
	 * This is the same as above with the only difference is that it accepts a new
	 * random IV for each message. It's used for group messages were the same key is
	 * used for encrypting all messages, unlike what we have in user-to-user
	 * messages.
	 * 
	 * @param Message to be encrypted.
	 * @param Key     to encrypt the message with. It's randomly generated.
	 * @param IV      to encrypt the message with. It's randomly generated.
	 * @return Encrypted message.
	 */
	public byte[] encryptWithAES(String message, SecretKey key, IvParameterSpec Iv) {
		byte[] cipherText = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			Iv = Iv == null ? new IvParameterSpec(key.getEncoded()) : Iv;
			cipher.init(Cipher.ENCRYPT_MODE, key, Iv);
			cipherText = cipher.doFinal(Base64.getEncoder().encode(message.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

	/**
	 * 
	 * @param Encrypted message.
	 * @return Decrypted message (plaintext).
	 */
	public byte[] decryptWithRSA(byte[] message) {

		byte[] plainText = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"),
					PSource.PSpecified.DEFAULT);
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(), oaepParams);
			plainText = Base64.getDecoder().decode(cipher.doFinal(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plainText;
	}

	/**
	 * This is used once the received message is decrypted with RSA and the AES key
	 * have been extracted.
	 * 
	 * @param Encrypted message.
	 * @param Key       used for encrypting the message.
	 * @return Decrypted message (plain text).
	 */
	public byte[] decryptWithAES(byte[] message, SecretKey key) {

		return decryptWithAES(message, key, null);
	}

	/**
	 * Same as above but in this case the IV is extracted, since groups have their
	 * secret key stored within them, they aren't sent with every message. Please
	 * read the manual for better understanding.
	 * 
	 * @param Encrypted message.
	 * @param Key       used to encrypt the message.
	 * @param Iv        used to encrypt the message.
	 * @return Decrypted message (plain text).
	 */
	public byte[] decryptWithAES(byte[] message, SecretKey key, IvParameterSpec Iv) {
		byte[] plainText = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			Iv = Iv == null ? new IvParameterSpec(key.getEncoded()) : Iv;
			cipher.init(Cipher.DECRYPT_MODE, key, Iv);
			plainText = Base64.getDecoder().decode(cipher.doFinal(message));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return plainText;
	}
}
