package set1;

import utils.ToByte;
import utils.Utils;

public class Challenge2 {

	public static void main(String[] args) {
		String s1 = "1c0111001f010100061a024b53535009181c";
		String s2 = "686974207468652062756c6c277320657965";
		byte[] b1 = ToByte.hexDecode(s1);
		byte[] b2 = ToByte.hexDecode(s2);
		byte[] res = new byte[b1.length];
		for (int i = 0; i < b1.length; i++) {
			res[i] = (byte) (b1[i] ^ b2[i]);
		}
		Utils.printByteArray(res);

	}

}
