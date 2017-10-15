package set3;

public class MersenneTwister {
	private static final int w = 32, n = 624, m = 397, r = 31;
	private static final int a = 0x9908B0DF;
	private static final int u = 11, d = 0xFFFFFFFF;
	private static final int s = 7, b = 0x9D2C5680;
	private static final int t = 15, c = 0xEFC60000;
	private static final int l = 18;

	private static final int f = 1812433253;

	private static final int LOWER_MASK = (1 << r) - 1;
	private static final int UPPER_MASK = ~LOWER_MASK;

	private int[] MT = new int[n];
	private int index = n + 1;

	public MersenneTwister(int seed) {
		index = n;
		MT[0] = seed;
		for (int i = 1; i < n; i++) {
			MT[i] = (f * (MT[i - 1] ^ (MT[i - 1] >> (w - 2))) + i);
		}
	}

	public int extractNumber() throws Error {
		if (index >= n) {
			if (index > n) {
				throw new Error("Generator was never seeded");
			}
			twist();
		}
		int y = MT[index];
		y ^= ((y >> u) & d);
		y ^= ((y << s) & b);
		y ^= ((y << t) & c);
		y ^= (y >> l);
		index++;
		return y;
	}

	private void twist() {
		for (int i = 0; i < n; i++) {
			int x = MT[i] & UPPER_MASK + (MT[(i + 1) % n] & LOWER_MASK);
			int xA = x >> 1;
			if(x % 2 != 0) {
				xA = xA ^ a;
			}
			MT[i] = MT[(i+m) % n] ^ xA;
		}
		index = 0;
	}
}
