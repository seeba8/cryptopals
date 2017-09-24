package set2;

import java.util.Random;

import utils.AES;
import utils.Utils;

public class Challenge11 {

	private static byte[] generateRandomKey() {
		byte[] rnd = new byte[16];
		new Random().nextBytes(rnd);
		return rnd;
	}

	private static byte[] encryptionOracle(byte[] input) {
		Random rnd = new Random();
		byte[] key = generateRandomKey();
		boolean cbc = rnd.nextBoolean();
		System.out.println("Actual mode: " + (cbc ? "CBC" : "ECB"));
		int prefixLength = rnd.nextInt(5) + 5;
		int suffixLength = rnd.nextInt(5) + 5;
		byte[] plain = new byte[input.length + prefixLength + suffixLength];
		for (int i = 0; i < plain.length; i++) {
			if (i < prefixLength || i >= prefixLength + input.length) {
				plain[i] = (byte) rnd.nextInt(256);
			} else {
				plain[i] = input[i - prefixLength];
			}
		}
		byte[] ciphertext = new byte[plain.length];
		if (cbc) {
			ciphertext = AES.cbcEncode(plain, key, generateRandomKey());
		} else {
			ciphertext = AES.ecbEncode(plain, key);
		}
		return ciphertext;
	}

	public static void main(String[] args) {
		// Equal plaintext blocks have equal ciphertext blocks in ECB
		// So if we push in enough equal plaintext, we can easily detect ECB
		byte[] ciphertext = encryptionOracle(new byte[128]);
		System.out.println("Oracle: " + (Utils.detectECBMode(ciphertext) ? "ECB" : "CBC"));
	}
}
