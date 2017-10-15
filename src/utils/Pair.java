package utils;

public class Pair implements Comparable<Pair> {
	public final int key;
	public final float value;

	public Pair(int key, float value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public int compareTo(Pair o) {
		if (this.value > o.value) {
			return 1;
		} else if (this.value < o.value) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return this.key + "=>" + this.value;
	}
}
