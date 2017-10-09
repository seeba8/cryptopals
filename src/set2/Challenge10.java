package set2;

import java.io.IOException;
import java.nio.file.Paths;

import utils.AES;
import utils.Utils;

public class Challenge10 {

	public static void main(String[] args) {
		byte[] key = "YELLOW SUBMARINE".getBytes();
		byte[] iv = new byte[16];
		byte[] ciphertext;
		try {
			ciphertext = utils.ToByte.readBase64File(Paths.get("src/set2/10.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] plain;
		try {
			plain = AES.cbcDecode(ciphertext, key, iv);
			System.out.println(Utils.bytesToString(plain));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
