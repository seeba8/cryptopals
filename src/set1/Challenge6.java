package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import utils.Utils;

public class Challenge6 {

	public static void main(String[] args) {
		try {
			byte[] cipher = Base64.getDecoder()
					.decode(String.join("", Files.readAllLines(Paths.get("src/set1/6.txt"))));
			byte[] bestKey = Utils.breakVigenere(cipher, 2, 40);
			System.out.println(Utils.bytesToString(bestKey));
			System.out.println(bestKey.length);
			System.out.println(Utils.bytesToString(Utils.repeatingKeyXOR(cipher, bestKey)));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
