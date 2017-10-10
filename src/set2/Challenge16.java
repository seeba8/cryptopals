package set2;

import utils.AES;
import utils.Utils;

public class Challenge16 {
	static byte[] key;
	static byte[] iv;

	private static byte[] padEncrypt(String in) {
		if (key == null) {
			key = Utils.generateRandomKey();
			iv = new byte[16];
		}
		String prefix = "comment1=cooking%20MCs;userdata=";
		String suffix = ";comment2=%20like%20a%20pound%20of%20bacon";
		in = in.replace(";", "\";\"").replace("=", "\"=\"");
		in = prefix + in + suffix;
		return AES.cbcPadEncode(in.getBytes(), key, iv);
	}

	private static byte[] decrypt(byte[] cipher) throws Exception {
		return AES.cbcDecodeUnpad(cipher, key, iv);
	}

	private static boolean isAdmin(byte[] in) {
		return new String(in).contains(";admin=true");
	}

	public static void main(String[] args) {

		/**
		 * ? (0011 1111) is only one bit away from both ; (0011 1011) and = (0011 1101),
		 * see http://www.neurophys.wisc.edu/comp/docs/ascii/ for an ascii table with
		 * bits.
		 * 
		 * That is why we get by only manipulating one bit at a time It would also work
		 * by iterating through each possible byte, but of course that is way more
		 * calculation intensive
		 */
		byte[] cipher = padEncrypt("aaaaaaaaaaaaaaaa?admin?true");
		for (int l = 0; l < cipher.length - 8; l++) {
			// for (int i = 0; i < 256; i++) {
			// for (int j = 0; j < 256; j++) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					byte[] rigged = cipher.clone();
					rigged[l] ^= 1 << 1 + j;
					rigged[l + 6] ^= 1 << 1 + i;
					// rigged[l] = (byte)i;
					// rigged[l+6] = (byte)j;
					byte[] out;
					try {
						out = decrypt(rigged);
						if (isAdmin(out)) {
							System.out.println(new String(out));
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

}
