package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import utils.ToByte;
import utils.Utils;
import utils.Vigenere;

public class Challenge4 {

	public static void main(String[] args) {
		byte[] bestRes = new byte[0];
		String bestString = "";
		List<String> lines = new LinkedList<String>();
		try {
			Files.lines(Paths.get("src/set1/4.txt")).forEach(lines::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String l : lines) {
			byte[] decoded = Utils.repeatingKeyXOR(ToByte.hexDecode(l),
					new byte[] { Vigenere.guessSingleCharXOR(ToByte.hexDecode(l)) });
			if (bestRes.length == 0 || Vigenere.rate(bestRes) < Vigenere.rate(decoded)) {
				// System.out.println(bytesToString(bestRes));
				bestRes = decoded.clone();
				bestString = l;
			}
		}
		Utils.prettyPrintByteArray(bestRes);
		System.out.println(bestString + "->" + Utils.bytesToString(bestRes));
	}

}
