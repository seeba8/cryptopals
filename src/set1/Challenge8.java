package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import utils.ToByte;
import utils.Utils;

public class Challenge8 {

	public static void main(String[] args) {
		System.out.println("Challenge 8");
		String[] lines;
		try {
			lines = Files.readAllLines(Paths.get("src/set1/8.txt")).toArray(new String[] {});
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		for (String line : lines) {
			byte[] ciphertext = ToByte.hexDecode(line);
			if (Utils.detectECBMode(ciphertext)) {
				System.out.println(line);
			}
		}
	}

}
