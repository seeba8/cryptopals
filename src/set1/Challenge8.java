package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
			byte[] ciphertext = Set1.hexDecode(line);
			for (int a = 0; a < ciphertext.length - 32; a += 16) {
				for (int b = a + 16; b < ciphertext.length - 16; b += 16) {
					if (Challenge6.hammingDistance(Arrays.copyOfRange(ciphertext, a, a + 16),
							Arrays.copyOfRange(ciphertext, b, b + 16)) == 0) {
						int l = 16;
						while (Challenge6.hammingDistance(Arrays.copyOfRange(ciphertext, a, a + l),
								Arrays.copyOfRange(ciphertext, b, b + l)) == 0) {
							l += 16;
						}
						System.out.println(l + ": " + line);
						System.out.print("repeating part: ");
						Set1.printByteArray(Arrays.copyOfRange(ciphertext, a, a + l));
					}
				}
			}
		}
	}

}
