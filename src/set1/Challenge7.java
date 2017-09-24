package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Challenge7 {

	public static byte[] readBase64File(Path p) throws IOException {
		return Base64.getDecoder().decode(String.join("", Files.readAllLines(p)));
	}

	public static void main(String[] args) {
		byte[] cipher;
		try {
			cipher = readBase64File(Paths.get("src/set1/7.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] key = "YELLOW SUBMARINE".getBytes();
		byte[] res = AES.decode(cipher, key);
		res = set2.Challenge9.removePkcs7Padding(res);
		System.out.println(Set1.bytesToString(res));
	}

}
