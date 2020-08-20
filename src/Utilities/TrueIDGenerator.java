package Utilities;

import java.security.SecureRandom;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class TrueIDGenerator {

	private static TrueIDGenerator instance;
	private SecureRandom TRNG;
	private final char[] characterPool;

	static {
		instance = new TrueIDGenerator();
	}

	private TrueIDGenerator() {
		TRNG = new SecureRandom();
		characterPool = new char[62];
		int i = 0;
		for (char c = 'a'; c < 'z' + 1; c++, i += 2) {
			characterPool[i] = c;
			characterPool[i + 1] = Character.toUpperCase(c);
		}
		for (int j = 0, k = 52; j < 10; j++, k++) {
			characterPool[k] = Character.forDigit(j, 10);
		}

	}

	public static TrueIDGenerator getInstance() {
		return instance;
	}

	public String generateID(int length) {
		char[] id = new char[length];
		for (int i = 0; i < length; i++) {
			id[i] = characterPool[TRNG.nextInt(characterPool.length)];
		}
		return new String(id);

	}

}
