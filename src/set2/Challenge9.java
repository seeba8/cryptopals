package set2;

import java.util.Arrays;

public class Challenge9 {

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

	public static byte[] removePkcs7Padding(byte[] input) {
		int len = input[input.length - 1];
		return Arrays.copyOf(input, input.length - len);
	}

	public static void main(String[] args) {
		byte[] input = "YELLOW SUBMARINE".getBytes();
		System.out.println(set1.Set1.bytesToString(addPkcs7Padding(input, 20)));
		set1.Set1.prettyPrintByteArray(addPkcs7Padding(input, 20));

	}

}
