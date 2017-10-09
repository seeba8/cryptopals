package set2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import utils.AES;
import utils.ToByte;
import utils.Utils;

public class Challenge12 {
	private static byte[] key;

	private static byte[] padEncrypt(final byte[] input) {
		if (key == null) {
			key = Utils.generateRandomKey();
		}
		final byte[] suffix = ToByte.base64Decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg"
				+ "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq"
				+ "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" + "YnkK");
		final byte[] plain = Arrays.copyOf(input, input.length + suffix.length);
		for (int i = 0; i < suffix.length; i++) {
			plain[input.length + i] = suffix[i];
		}

		final byte[] out = AES.ecbEncode(plain, key);

		return out;
	}

	private static byte[] breakPaddingOracle(final Function<byte[], byte[]> oracle) {
		final int blockSize = Utils.detectBlockSize(oracle);
		assert (Utils.isECBMode(oracle));
		boolean finished = false;
		final ArrayList<Byte> l = new ArrayList<Byte>();
		while (!finished) {
			final int prevSize = l.size();
			finished = true;
			final byte[] input = new byte[blockSize - (prevSize % blockSize) - 1];
			final byte[] currentBlock = getCurrentBlock(oracle.apply(input), blockSize, prevSize);
			final byte[] rigged = new byte[blockSize];
			for (int i = 0; (blockSize - 2 - i >= 0) && (prevSize - i - 1 > -1) && prevSize != 0; i++) {
				rigged[blockSize - 2 - i] = l.get(prevSize - i - 1);
			}
			for (int j = 0; j < 256; j++) {
				rigged[blockSize - 1] = (byte) j;
				final byte[] resBlock = Arrays.copyOfRange(oracle.apply(rigged.clone()), 0, blockSize);
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

	private static byte[] getCurrentBlock(final byte[] cipher, final int blockSize, final int prevSize) {
		final int blockNo = prevSize / blockSize;
		return Arrays.copyOfRange(cipher, blockNo * blockSize, (blockNo + 1) * blockSize);
	}

	public static void main(final String[] args) {
		final byte[] res = Utils.removePkcs7Padding(breakPaddingOracle(Challenge12::padEncrypt));
		System.out.println(new String(res));

		System.out.println("OR");

		System.out
				.println(new String(Utils.removePkcs7Padding(Challenge14.breakPaddingOracle(Challenge12::padEncrypt))));
	}

}
