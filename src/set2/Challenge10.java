package set2;

public class Challenge10 {

	public static void main(String[] args) {
		byte[] key = "YELLOW SUBMARINE".getBytes();
		byte[] clear = "123456789ABCDEF0".getBytes();
		byte[] cipher = set1.AES.encode(clear, key);
		byte[] res = set1.AES.decode(cipher, key);
		System.out.println(set1.Set1.bytesToString(res));

	}

}
