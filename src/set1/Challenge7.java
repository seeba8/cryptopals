package set1;

import java.io.IOException;
import java.nio.file.Paths;

import utils.AES;
import utils.ToByte;
import utils.Utils;

public class Challenge7 {

	public static void main(String[] args) {
		byte[] cipher;
		try {
			cipher = ToByte.readBase64File(Paths.get("src/set1/7.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] key = "YELLOW SUBMARINE".getBytes();
		byte[] res;
		try {
			res = AES.ecbDecode(cipher, key);
			System.out.println(Utils.bytesToString(res));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
