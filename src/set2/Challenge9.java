package set2;

import utils.Utils;

public class Challenge9 {

	public static void main(String[] args) {
		byte[] input = "YELLOW SUBMARINE".getBytes();
		System.out.println(Utils.bytesToString(Utils.addPkcs7Padding(input, 20)));
		Utils.prettyPrintByteArray(Utils.addPkcs7Padding(input, 20));

	}

}
