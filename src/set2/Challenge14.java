package set2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import utils.AES;
import utils.ToByte;
import utils.Utils;

public class Challenge14 {
	static byte[] key;
	static byte[] prefix;

	private static byte[] padEncrypt(final byte[] input) {
		if (key == null) {
			key = Utils.generateRandomKey();
		}
		if (prefix == null) {
			prefix = new byte[new Random().nextInt(192) + 64];
			new Random().nextBytes(prefix);
		}
		final byte[] suffix = ToByte.base64Decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg"
				+ "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq"
				+ "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" + "YnkK");
		byte[] plain = Arrays.copyOf(prefix, prefix.length + input.length + suffix.length);
		System.arraycopy(input, 0, plain, prefix.length, input.length);
		System.arraycopy(suffix, 0, plain, prefix.length + input.length, suffix.length);
		return AES.ecbEncode(plain, key);
	}

	/**
	 * Finds the first block that changes when adding bytes to the oracle (this is
	 * the last block of the prefix)
	 * 
	 * @param oracle
	 * @param blockSize
	 * @return
	 */
	private static int detectPrefixBlockSize(Function<byte[], byte[]> oracle, int blockSize) {
		byte[] prev = oracle.apply(new byte[0]);
		byte[] next = oracle.apply(new byte[1]);
		for (int i = 0; i < next.length; i++) {
			if (next[i] != prev[i]) {
				return i / blockSize;
			}
		}
		return 0;
	}

	/**
	 * Detects the actual prefix size. After finding the last block of the prefix,
	 * it adds one byte until that block stays the same. Then, it subtracts that
	 * number of bytes from the blocklength, as those bytes pad the prefix to a full
	 * block.
	 * 
	 * @param oracle
	 * @param blockSize
	 * @return
	 */
	public static int detectPrefixSize(Function<byte[], byte[]> oracle, int blockSize) {
		int prefixBlocks = detectPrefixBlockSize(oracle, blockSize);
		byte[] lastPrefixBlock = Arrays.copyOfRange(oracle.apply(new byte[0]), prefixBlocks * blockSize,
				(prefixBlocks + 1) * blockSize);
		for (int i = 1; i <= blockSize; i++) {
			byte[] currLastPrefixBlock = Arrays.copyOfRange(oracle.apply(new byte[i]), prefixBlocks * blockSize,
					(prefixBlocks + 1) * blockSize);
			if (Arrays.equals(lastPrefixBlock, currLastPrefixBlock)) {
				return blockSize * prefixBlocks + blockSize - i + 1;
			}
			lastPrefixBlock = currLastPrefixBlock.clone();
		}

		return prefixBlocks * blockSize;
	}

	public static byte[] breakPaddingOracle(final Function<byte[], byte[]> oracle) {
		final int blockSize = Utils.detectBlockSize(oracle);
		assert (Utils.isECBMode(oracle));
		final int prefixSize = detectPrefixSize(oracle, blockSize);
		final int prefixOverlap = prefixSize % blockSize;
		final int padPrefix = blockSize - prefixOverlap;
		final int prefixBlocks = (prefixSize / blockSize) + 1;
		boolean finished = false;
		final ArrayList<Byte> l = new ArrayList<Byte>();
		while (!finished) {
			final int prevSize = l.size();
			finished = true;

			/**
			 * Create an empty byte array so that it buffers the padding (if it exists) to a
			 * full block. Additionally, the empty array is one unknown byte short of an
			 * extra block, so that the first unknown byte of the padding is included in
			 * that otherwise empty block. Then, just save that block.
			 */
			final byte[] input = new byte[padPrefix + blockSize - (prevSize % blockSize) - 1];
			final byte[] currentBlock = getCurrentBlock(oracle.apply(input), blockSize, prevSize, prefixBlocks);

			/**
			 * Create a "rigged" input array which has the length of the padPrefix (to
			 * "ignore" the prefix) plus a full block. Fill this, except the last byte, with
			 * the knowledge about the previous bytes of the suffix padding (if we know
			 * anything already)
			 */
			final byte[] rigged = new byte[padPrefix + blockSize];
			for (int i = 0; (blockSize - 2 - i >= 0) && (prevSize - i - 1 > -1) && prevSize != 0; i++) {
				rigged[padPrefix + blockSize - 2 - i] = l.get(prevSize - i - 1);
			}
			/**
			 * Then loop through all possible values of a byte and set the last byte of the
			 * rigged array to that byte. Check if the block equals the encrypted "empty
			 * minus one" array. If it does, we know the next byte of the suffix padding,
			 * and we can save it for the next round.
			 */
			for (int j = 0; j < 256; j++) {
				rigged[padPrefix + blockSize - 1] = (byte) j;
				final byte[] resBlock = Arrays.copyOfRange(oracle.apply(rigged.clone()), prefixBlocks * blockSize,
						(prefixBlocks + 1) * blockSize);
				if (Arrays.equals(resBlock, currentBlock)) {
					l.add((byte) j);
					finished = false;
					break;
				}
			}
		}
		final byte[] res = new byte[l.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = l.get(i);
		}
		return res;
	}

	private static byte[] getCurrentBlock(final byte[] cipher, final int blockSize, final int prevSize,
			final int prefixBlocks) {
		final int blockNo = prevSize / blockSize + prefixBlocks;
		return Arrays.copyOfRange(cipher, blockNo * blockSize, (blockNo + 1) * blockSize);
	}

	public static void main(String[] args) {
		try {
			System.out.println(new String(Utils.removePkcs7Padding(breakPaddingOracle(Challenge14::padEncrypt))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
