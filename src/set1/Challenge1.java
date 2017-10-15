package set1;

import utils.ToByte;
import utils.Utils;

public class Challenge1 {

	public static void main(String[] args) {
		String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		byte[] b = ToByte.hexDecode(input);
		String s = Utils.bytesToString(b);
		System.out.println(s);
		System.out.println(Utils.toBase64(b));
	}

}
