package set3;

import utils.AES;
import utils.ToByte;

public class Challenge18 {

	public static void main(final String[] args) {
		final byte[] key = "YELLOW SUBMARINE".getBytes();
		long nonce = 0;
		byte[] ciphertext = ToByte
				.base64Decode("L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ==");
		System.out.println(new String(AES.CTR(ciphertext,key,nonce)));
	}

}
