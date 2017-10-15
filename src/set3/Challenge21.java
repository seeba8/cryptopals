package set3;

public class Challenge21 {

	public static void main(String[] args) {
		MersenneTwister mt = new MersenneTwister(5);
		for (int i = 0; i < 10; i++) {
			System.out.println(mt.extractNumber());
		}

	}

}
