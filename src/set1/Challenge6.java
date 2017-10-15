package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import utils.Utils;
import utils.Vigenere;

public class Challenge6 {

	public static void main(String[] args) {
		try {
			byte[] cipher = Base64.getDecoder()
					.decode(String.join("", Files.readAllLines(Paths.get("src/set1/6.txt"))));
			byte[] bestKey = Vigenere.breakVigenere(cipher, 2, 40);
			System.out.println(Utils.bytesToString(bestKey));
			System.out.println(bestKey.length);
			System.out.println(Utils.bytesToString(Utils.repeatingKeyXOR(cipher, bestKey)));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
