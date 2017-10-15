package set3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import utils.AES;
import utils.ToByte;
import utils.Utils;
import utils.Vigenere;

public class Challenge20 {

	public static void main(String[] args) throws IOException {
		System.out.println("Does not work perfectly every time. Sometimes it needs to run multiple times");
		byte[] key = Utils.generateRandomKey();
		ArrayList<String> lines = new ArrayList<String>();
		Files.lines(Paths.get("src/set3/20.txt")).forEach(lines::add);
		int minLen = Integer.MAX_VALUE;
		byte[][] ciphertexts = new byte[lines.size()][];
		int c = 0;
		for(String s : lines) {
			ciphertexts[c] = AES.CTR(ToByte.base64Decode(s), key, 0);
			if(ciphertexts[c].length < minLen) {
				minLen = ciphertexts[c].length;
			}
			c++;
		}
		byte[] concatenation = new byte[minLen * ciphertexts.length];
		for(int i = 0; i < ciphertexts.length; i++) {
			System.arraycopy(ciphertexts[i], 0, concatenation, i*minLen, minLen);
		}
		byte[] guess = Vigenere.breakVigenere(concatenation.clone(), minLen, minLen);
		byte[] plain = Utils.repeatingKeyXOR(concatenation, guess);
		for(int i = 0; i < ciphertexts.length; i++) {
			System.out.println(new String(Arrays.copyOfRange(plain, i*minLen, (i+1)*minLen)));
		}
	}

}
