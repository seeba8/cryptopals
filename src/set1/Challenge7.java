package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class Challenge7 {

	public static byte[] readBase64File(Path p) throws IOException {
		return Base64.getDecoder().decode(String.join("", Files.readAllLines(p)));
	}

	 private static void rotate(byte[] in) {
		 byte a = in[0];
		 for(int i = 0; i < in.length - 1; i++) {
			 in[i] = in[i+1];
		 }
		 in[in.length-1] = a;
	 }

	private static void keyExpansion(byte[] key) throws Exception {
		int b;
		switch (key.length * 8) {
		case 128:
			b = 176;
			break;
		case 192:
			b = 208;
			break;
		case 256:
			b = 240;
			break;
		default:
			throw new Exception("Invalid key length");
		}
		int n = key.length / 8;
		byte[] expanded = Arrays.copyOf(key, b);
		int progress = key.length;
		int i = 0; // rcon iteration value
		while (progress < b) {
			byte[] t = Arrays.copyOfRange(expanded, progress - 4, progress);
			t = keyScheduleCore(t, i++);
			byte[] res = Set1.repeatingKeyXOR(t, Arrays.copyOfRange(expanded, progress - n, progress - n + 4));
		}
	}

	private static byte[] keyScheduleCore(byte[] in, int i) {
		byte[] out = in.clone();
		rotate(out);
		return out;
	}

	public static byte[] AES(byte[] data, byte[] key) throws Exception {
		int repetitions;
		switch (key.length * 8) { // in bits
		case 128:
			repetitions = 10;
			break;
		case 192:
			repetitions = 12;
			break;
		case 256:
			repetitions = 14;
			break;
		default:
			throw new Exception("Invalid key length");
		}
		byte[] result = new byte[data.length];
		return result;
	}

	public static void main(String[] args) {
		byte[] cipher;
		try {
			cipher = readBase64File(Paths.get("src/set1/7.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] key = "YELLOW SUBMARINE".getBytes();
		try {
			AES(cipher, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
