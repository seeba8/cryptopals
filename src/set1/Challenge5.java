package set1;

import utils.Utils;

public class Challenge5 {

	public static void main(String[] args) {
		String line = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
		String key = "ICE";
		byte[] k = key.getBytes();
		byte[] l = line.getBytes();
		// printByteArray(l, true);
		Utils.printByteArray(Utils.repeatingKeyXOR(l, k));
		System.out.println(Utils.repeatingKeyXOR(l, k));
	}

}
