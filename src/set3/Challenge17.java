package set3;

import java.util.Arrays;
import java.util.Random;

import utils.AES;
import utils.ToByte;
import utils.Utils;

public class Challenge17 {
	static byte[][] plains;
	static byte[] key;
	static byte[] iv;

	private static byte[] encrypt() {
		if (key == null) {
			key = Utils.generateRandomKey();
			iv = new byte[16];
		}
		byte[] plain = plains[new Random().nextInt(plains.length)];
		return AES.cbcPadEncode(plain, key, iv);
	}

	private static boolean checkValidity(byte[] cipher, byte[] iv) {
		byte[] res = AES.cbcDecode(cipher, key, iv);
		// Utils.prettyPrintByteArray(Arrays.copyOfRange(res,res.length-16,
		// res.length));
		try {
			Utils.removePkcs7Padding(res);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static byte[] decryptBlock(byte[] previous, byte[] block) {
		byte[] decrypted = new byte[16];
		for (int i = 15; i >= 0; i--) {
			int pad = 16 - i;
			int correctByte = -1;
			for (int j = 0; j < 256; j++) {
				byte[] c = previous.clone();
				decrypted[i] = (byte) j;
				for (int k = 0; k < pad; k++) {
					c[i + k] ^= decrypted[i + k] ^ pad;
				}
				if (checkValidity(block, c)) {
					if (correctByte == -1) {
						correctByte = j;
					} else {
						// One of the two paddings it detects might be the correct one from the last block.
						// To check, we mangle the previous byte so that it is highly unlikely (impossible?)
						// to stay a valid padding.
						c[i-1] ^= 0xa0;
						if(checkValidity(block, c)) {
							correctByte = j;
						}
						break;
					}
					
				}
			}
			decrypted[i] = (byte) correctByte;
		}
		return decrypted;
	}

	public static byte[] CBCPaddingOracle(byte[] ciphertext) {
		byte[] plaintext = new byte[ciphertext.length];
		for (int i = 0; i < ciphertext.length / 16; i++) {
			byte[] res;
			if (i == 0) {
				res = decryptBlock(iv.clone(), Arrays.copyOfRange(ciphertext, i * 16, (i + 1) * 16));
			} else {
				res = decryptBlock(Arrays.copyOfRange(ciphertext, (i - 1) * 16, i * 16),
						Arrays.copyOfRange(ciphertext, i * 16, (i + 1) * 16));
			}
			System.arraycopy(res, 0, plaintext, i * 16, 16);
		}
		System.out.println(new String(plaintext));
		return null;
	}

	public static void main(String[] args) {
		prepare();
		byte[] ciphertext = encrypt();
		CBCPaddingOracle(ciphertext);
	}

	private static void prepare() {
		String[] plainStrings = { "MDAwMDAwTm93IHRoYXQgdGhlIHBhcnR5IGlzIGp1bXBpbmc=",
				"MDAwMDAxV2l0aCB0aGUgYmFzcyBraWNrZWQgaW4gYW5kIHRoZSBWZWdhJ3MgYXJlIHB1bXBpbic=",
				"MDAwMDAyUXVpY2sgdG8gdGhlIHBvaW50LCB0byB0aGUgcG9pbnQsIG5vIGZha2luZw==",
				"MDAwMDAzQ29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg==",
				"MDAwMDA0QnVybmluZyAnZW0sIGlmIHlvdSBhaW4ndCBxdWljayBhbmQgbmltYmxl",
				"MDAwMDA1SSBnbyBjcmF6eSB3aGVuIEkgaGVhciBhIGN5bWJhbA==",
				"MDAwMDA2QW5kIGEgaGlnaCBoYXQgd2l0aCBhIHNvdXBlZCB1cCB0ZW1wbw==",
				"MDAwMDA3SSdtIG9uIGEgcm9sbCwgaXQncyB0aW1lIHRvIGdvIHNvbG8=",
				"MDAwMDA4b2xsaW4nIGluIG15IGZpdmUgcG9pbnQgb2g=",
				"MDAwMDA5aXRoIG15IHJhZy10b3AgZG93biBzbyBteSBoYWlyIGNhbiBibG93" };
		plains = new byte[plainStrings.length][];
		for (int i = 0; i < plains.length; i++) {
			plains[i] = ToByte.base64Decode(plainStrings[i]);
		}
	}

}
