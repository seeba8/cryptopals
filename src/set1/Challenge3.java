package set1;

import utils.ToByte;
import utils.Utils;
import utils.Vigenere;

public class Challenge3 {

	public static void main(String[] args) {
		String cipher = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		byte key = Vigenere.guessSingleCharXOR(ToByte.hexDecode(cipher));
		byte[] bestResult = Utils.repeatingKeyXOR(ToByte.hexDecode(cipher), new byte[] { key });
		System.out.println(Utils.bytesToString(bestResult));
	}

}
