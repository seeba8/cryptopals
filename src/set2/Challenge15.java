package set2;

import utils.Utils;

public class Challenge15 {

	public static void main(String[] args) {
		byte[][] paddings = { { 4, 4, 4, 4 }, { 5, 5, 5, 5 }, { 1, 2, 3, 4 } };
		byte[] baby = "ICE ICE BABY".getBytes();
		for (byte[] pad : paddings) {
			try {
				System.out.println(new String(Utils.removePkcs7Padding(Utils.concatArrays(baby, pad))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
