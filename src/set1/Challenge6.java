package set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class Challenge6 {
	
	public static int hammingDistance(byte[] a, byte[] b) {
		int dist = 0;
		if (a.length > b.length) {
			b = padWithZeros(b, a.length);
		} else if(b.length > a.length) {
			a = padWithZeros(a, b.length);
		}
		
		for(int i = 0; i < a.length; i++) {
			dist += Integer.bitCount((b[i] ^ a[i]));
		}
		return dist;
	}
	
	private static byte[] padWithZeros(byte[] b, int length) {
		byte[] res = new byte[length];
		for(int i = 0; i < b.length; i++) {
			res[i] = b[i];
		}
		return res;
	}
	
	public static Pair[] orderKeySizeGuesses(byte[] input, int minKeyLength, int maxKeyLength) {
		ArrayList<Pair> list = new ArrayList<Pair>();
		for(int i = minKeyLength; i < maxKeyLength+1; i++) {
			byte[][] examples = new byte[4][i];
			float score = 0;
			for(int j = 0; j < examples.length; j++) {
				examples[j] = Arrays.copyOfRange(input, j*i, (j+1)*i);
			}
			for(int j = 0; j < examples.length; j++) {
				for (int k = 0; k < examples.length; k++) {
					score += (1./6.) * (hammingDistance(examples[j], examples[k])/i);
				}
			}
//			list.add(new Pair(i,(float)hammingDistance(Arrays.copyOfRange(input, 0, i),
//						Arrays.copyOfRange(input, i, 2*i))/i));
			list.add(new Pair(i, score));
		}
		Collections.sort(list);
		return list.toArray(new Pair[list.size()]);
	}

	public static byte[] decryptXOR(byte[] cipher) {
		byte[] plaintext = new byte[cipher.length];
		
		return plaintext;
	}
	
	public static byte[][] breakIntoBlocks(byte[] data, int blocksize) {
		byte[][] blocks = new byte[(int)Math.ceil((float)data.length / blocksize)][blocksize];
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = Arrays.copyOfRange(data, i*blocksize,(i+1)*blocksize);
		}
		return blocks;
	}
	
	public static byte[][] transposeBlocks(byte[][] blocks) {
		byte[][] transposed = new byte[blocks[0].length][blocks.length];
		for(int outer = 0; outer < transposed.length; outer++) {
			for(int inner = 0; inner < transposed[0].length; inner++) {
				transposed[outer][inner] = blocks[inner][outer];
			}
		}
		return transposed;
	}
	
	public static byte[] breakVigenere(byte[] cipher, int minKeyLength, int maxKeyLength) {
		Pair[] order = orderKeySizeGuesses(cipher, 2, 40);
//		System.out.println(Arrays.deepToString(order));
		byte[] bestKey = new byte[0];
		float bestRating = 0;
		for(Pair p: order) {
			byte[] key = new byte[p.key];
			byte[][] transposed = transposeBlocks(breakIntoBlocks(cipher,key.length));
			for(int i = 0; i < key.length; i++) {
				key[i] = Set1.guessSingleCharXOR(transposed[i]);
			}
			byte[] plain = Set1.repeatingKeyXOR(cipher, key);
			float rating = Set1.rate(plain);
			if(bestRating == 0 || rating > bestRating) {
				bestRating = rating;
				bestKey = key.clone();
//				System.out.println(Set1.bytesToString(bestPlain));
			}	
		}
		return bestKey;
		
	}

	public static void main(String[] args) {
		try {
			byte[] cipher = Base64.getDecoder()
					.decode(String.join("", Files.readAllLines(Paths.get("src/set1/6.txt"))));
			byte[] bestKey = breakVigenere(cipher, 2, 40);
			System.out.println(Set1.bytesToString(bestKey));
			System.out.println(bestKey.length);
			System.out.println(Set1.bytesToString(Set1.repeatingKeyXOR(cipher, bestKey)));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
