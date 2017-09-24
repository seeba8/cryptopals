package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class Set1 {

	public static String toBase64(byte[] input) {
		return Base64.getEncoder().encodeToString(input);
	}

	public static String bytesToString(byte[] b) {
		String s = "";
		for (int i = 0; i < b.length; i++) {
			s += (char) (b[i]);
		}
		return s;
	}

	public static byte[] hexDecode(String str) {
		byte[] b = new byte[(int) Math.ceil(str.length() / 2)];
		for (int i = 0; i < str.length(); i += 2) {
			b[i / 2] = (byte) Integer.parseInt(str.substring(i, i + 2), 16);
		}
		return b;
	}

	public static void printByteArray(byte[] arr) {
		int c = 0;
		for (byte b : arr) {
			if(c != 0 && c % 16 == 0) {
				System.out.print("\n");
			}
			System.out.print(String.format("%02x ", b));
			c++;
		}
		System.out.print("\n");
	}

	public static float rate(byte[] res) {
		int normalLetters = 0;
		int spaces = 0;
		for (byte b : res) {
			// System.out.println(b);
			if ((b > 64 && b < 91) || (b > 96 && b < 123)) {
				normalLetters++;
			} else if (b < 65 && b != 32 && b != 39 && b != 44 && b != 46) {
				normalLetters -= 5;
			} else {
				normalLetters--;
			}
			if (b == 32)
				spaces++;
		}
		if (spaces == 0 || (float) res.length / spaces > 9) {
			return 0f;
		}
		return (float) normalLetters / res.length;
	}

	public static byte[] repeatingKeyXOR(byte[] plain, byte[] key) {
		byte[] cipher = new byte[plain.length];
		for (int i = 0; i < cipher.length; i++) {
			cipher[i] = (byte) (plain[i] ^ key[i % key.length]);
		}
		return cipher;
	}

	public static void challenge5() {
		String line = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
		String key = "ICE";
		byte[] k = key.getBytes();
		byte[] l = line.getBytes();
		// printByteArray(l, true);
		printByteArray(repeatingKeyXOR(l, k));
		System.out.println(repeatingKeyXOR(l, k));
	}

	public static void challenge4() {
		byte[] bestRes = new byte[0];
		String bestString = "";
		List<String> lines = new LinkedList<String>();
		try {
			Files.lines(Paths.get("src/set1/4.txt")).forEach(lines::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String l : lines) {
			byte[] decoded = repeatingKeyXOR(hexDecode(l), new byte[] { guessSingleCharXOR(l) });
			if (bestRes.length == 0 || rate(bestRes) < rate(decoded)) {
				// System.out.println(bytesToString(bestRes));
				bestRes = decoded.clone();
				bestString = l;
			}
		}
		System.out.println(bestString + "->" + bytesToString(bestRes));
	}

	public static byte guessSingleCharXOR(byte[] b) {
		byte[] res = new byte[b.length];
		byte[] bestResult = new byte[0];
		byte bestKey = 0;
		for (int x = 0; x < 256; x++) {
			for (int i = 0; i < b.length; i++) {
				res[i] = (byte) (b[i] ^ x);
			}
			if (bestResult.length == 0 || rate(res) > rate(bestResult)) {
				bestResult = res.clone();
				bestKey = (byte) x;
			}
		}
		return bestKey;
	}

	public static byte guessSingleCharXOR(String input) {
		byte[] b = hexDecode(input);
		return guessSingleCharXOR(b);
	}

	public static void challenge3() {
		String cipher = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		byte key = guessSingleCharXOR(cipher);
		byte[] bestResult = repeatingKeyXOR(hexDecode(cipher), new byte[] { key });
		System.out.println(bytesToString(bestResult));
	}

	public static void challenge2() {
		System.out.println("\nChallenge 2:");
		String s1 = "1c0111001f010100061a024b53535009181c";
		String s2 = "686974207468652062756c6c277320657965";
		byte[] b1 = hexDecode(s1);
		byte[] b2 = hexDecode(s2);
		byte[] res = new byte[b1.length];
		for (int i = 0; i < b1.length; i++) {
			res[i] = (byte) (b1[i] ^ b2[i]);
		}
		printByteArray(res);

	}

	public static void challenge1() {
		System.out.println("\nChallenge 1:");
		String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		byte[] b = hexDecode(input);
		String s = bytesToString(b);
		System.out.println(s);
		System.out.println(toBase64(b));
	}

	public static void main(String[] args) {
		challenge4();
	}

}
