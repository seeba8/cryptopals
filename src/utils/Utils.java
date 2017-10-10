package utils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.function.Function;

public class Utils {
	public static String toBase64(byte[] input) {
		return Base64.getEncoder().encodeToString(input);
	}

	public static String bytesToString(byte[] b) {
		String s = "";
		if (b == null)
			return s;
		for (int i = 0; i < b.length; i++) {
			s += (char) (b[i]);
		}
		return s;
	}

	public static void printByteArray(byte[] arr) {
		for (byte b : arr) {
			System.out.print(String.format("%02x", b));
		}
		System.out.print("\n");
	}

	public static void prettyPrintByteArray(byte[] arr) {
		int c = 0;
		for (byte b : arr) {
			if (c != 0 && c % 16 == 0) {
				System.out.print("\n");
			}
			System.out.print(String.format("%02x ", b));
			c++;
		}
		System.out.print("\n");
	}

	public static byte[] repeatingKeyXOR(byte[] plain, byte[] key) {
		byte[] cipher = new byte[plain.length];
		for (int i = 0; i < cipher.length; i++) {
			cipher[i] = (byte) (plain[i] ^ key[i % key.length]);
		}
		return cipher;
	}

	public static int hammingDistance(byte[] a, byte[] b) {
		int dist = 0;
		if (a.length > b.length) {
			b = padWithZeros(b, a.length);
		} else if (b.length > a.length) {
			a = padWithZeros(a, b.length);
		}

		for (int i = 0; i < a.length; i++) {
			dist += Integer.bitCount((b[i] ^ a[i]));
		}
		return dist;
	}

	private static byte[] padWithZeros(byte[] b, int length) {
		byte[] res = new byte[length];
		for (int i = 0; i < b.length; i++) {
			res[i] = b[i];
		}
		return res;
	}

	public static byte[] addPkcs7Padding(byte[] input, int blockLength) {
		int newLength = input.length < blockLength ? blockLength
				: (int) Math.ceil((float) input.length / blockLength) * blockLength;
		if (input.length == newLength) {
			newLength += blockLength;
		}
		byte[] out = Arrays.copyOf(input, newLength);
		byte pad = (byte) (out.length - input.length);
		for (int i = input.length; i < out.length; i++) {
			out[i] = pad;
		}
		return out;
	}

	public static byte[] removePkcs7Padding(byte[] input) throws Exception {
		int len = input[input.length - 1];
		if (len > input.length || len <= 0) {
			throw new Exception("Bad padding");
		}
		for (int i = 0; i < len; i++) {
			if (input[input.length - 1 - i] != len) {
				throw new Exception("Bad padding");
			}
		}
		return Arrays.copyOf(input, input.length - len);
	}

	public static boolean detectECBMode(byte[] ciphertext) {
		for (int a = 0; a < ciphertext.length - 32; a += 16) {
			for (int b = a + 16; b < ciphertext.length - 16; b += 16) {
				if (Utils.hammingDistance(Arrays.copyOfRange(ciphertext, a, a + 16),
						Arrays.copyOfRange(ciphertext, b, b + 16)) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static byte[] generateRandomKey() {
		byte[] rnd = new byte[16];
		new Random().nextBytes(rnd);
		return rnd;
	}

	public static int detectBlockSize(final Function<byte[], byte[]> func) {
		int block1Start = 0;
		int block2Start = 0;
		int c = 0;
		int prevLen = 0;
		while (block2Start == 0) {
			final int len = func.apply(new byte[++c]).length;
			if (prevLen == 0) {
				prevLen = len;
				continue;
			}
			if (len > prevLen) {
				if (block1Start == 0) {
					prevLen = len;
					block1Start = c;
				} else {
					block2Start = c;
				}
			}
		}
		return block2Start - block1Start;
	}

	public static boolean isECBMode(final Function<byte[], byte[]> func) {
		return Utils.detectECBMode(func.apply(new byte[128]));
	}

	public static byte[] concatArrays(byte[]... arrays) {
		if (arrays.length == 0) {
			return null;
		}
		if (arrays.length == 1) {
			return arrays[0];
		}
		int totalLength = 0;
		for (byte[] arr : arrays) {
			totalLength += arr.length;
		}
		byte[] result = new byte[totalLength];
		int offset = 0;
		for (byte[] array : arrays) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

}
