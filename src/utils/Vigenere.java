package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Vigenere {
	private static Pair[] orderKeySizeGuesses(byte[] input, int minKeyLength, int maxKeyLength) {
		ArrayList<Pair> list = new ArrayList<Pair>();
		for (int i = minKeyLength; i < maxKeyLength + 1; i++) {
			byte[][] examples = new byte[4][i];
			float score = 0;
			for (int j = 0; j < examples.length; j++) {
				examples[j] = Arrays.copyOfRange(input, j * i, (j + 1) * i);
			}
			for (int j = 0; j < examples.length; j++) {
				for (int k = 0; k < examples.length; k++) {
					score += (1. / 6.) * (Utils.hammingDistance(examples[j], examples[k]) / i);
				}
			}
			list.add(new Pair(i, score));
		}
		Collections.sort(list);
		return list.toArray(new Pair[list.size()]);
	}

	private static byte[][] breakIntoBlocks(byte[] data, int blocksize) {
		byte[][] blocks = new byte[(int) Math.ceil((float) data.length / blocksize)][blocksize];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = Arrays.copyOfRange(data, i * blocksize, (i + 1) * blocksize);
		}
		return blocks;
	}

	private static byte[][] transposeBlocks(byte[][] blocks) {
		byte[][] transposed = new byte[blocks[0].length][blocks.length];
		for (int outer = 0; outer < transposed.length; outer++) {
			for (int inner = 0; inner < transposed[0].length; inner++) {
				transposed[outer][inner] = blocks[inner][outer];
			}
		}
		return transposed;
	}

	public static byte[] breakVigenere(byte[] cipher, int minKeyLength, int maxKeyLength) {
		Pair[] order = orderKeySizeGuesses(cipher, minKeyLength, maxKeyLength);
		byte[] bestKey = new byte[0];
		float bestRating = 0;
		for (Pair p : order) {
			byte[] key = new byte[p.key];
			byte[][] transposed = transposeBlocks(breakIntoBlocks(cipher, key.length));
			for (int i = 0; i < key.length; i++) {
				key[i] = guessSingleCharXOR(transposed[i]);
			}
			byte[] plain = Utils.repeatingKeyXOR(cipher, key);
			float rating = rate(plain);
			if (bestRating == 0 || rating > bestRating) {
				bestRating = rating;
				bestKey = key.clone();
			}
		}
		return bestKey;
	}
	
	public static float rate(byte[] res) {
		int normalLetters = 0;
		for (byte b : res) {
			if(b < 0 || b > 122) return 0f;
			if((b < 32 && b != 10 && b != 13)) {
				normalLetters -= 10;
			} else if ((b > 64 && b < 91) || (b > 96 && b < 123)) {
				normalLetters++;
			} else if(b == 32 || b == 39 || b ==44 || b == 46) { 
				normalLetters++;
			} else if (b < 65 && b != 47) {
				normalLetters -= 5;
			} else if(b > 90 && b < 97) {
				normalLetters -= 5;
			} else {
				normalLetters--;
			}
		}
		return (float) normalLetters / res.length;
	}
	
	public static byte guessSingleCharXOR(byte[] b) {
		byte[] res = new byte[b.length];
		byte[] bestResult = new byte[0];
		byte bestKey = 0;
		for (int x = 0; x < 256; x++) {
			for (int i = 0; i < b.length; i++) {
				res[i] = (byte) (b[i] ^ x);
			}
			if (bestResult.length == 0 || rate(res) > rate(bestResult)) {
				bestResult = res.clone();
				bestKey = (byte) x;
			}
		}
		return bestKey;
	}
}
