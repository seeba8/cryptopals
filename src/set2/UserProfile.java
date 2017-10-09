package set2;

import java.util.HashMap;

import utils.AES;
import utils.Utils;

public class UserProfile extends HashMap<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int lastUID = 9;
	public static byte[] key;

	private UserProfile() {
	}

	public boolean isAdmin() {
		return (get("role").equals("admin"));
	}

	public UserProfile(String email) {
		put("email", email.replace("&", "").replace("=", ""));
		put("uid", String.valueOf(++lastUID));
		put("role", "user");
	}

	public String encode() {
		return String.format("email=%s&uid=%s&role=%s", get("email"), get("uid"), get("role"));
	}

	public byte[] encrypt() {
		if (key == null) {
			System.out.println("generating");
			key = Utils.generateRandomKey();
		}
		return AES.ecbEncode(encode().getBytes(), key);
	}

	public static UserProfile decryptAndParse(byte[] cipher) throws Exception {
		byte[] plain = AES.ecbDecode(cipher, key);
		return parse(new String(plain));
	}

	@Override
	public String toString() {
		return encode();
	}

	public static byte[] profileFor(String email) {
		UserProfile u = new UserProfile(email);
		// System.out.println(u.encode());
		return u.encrypt();
	}

	public static UserProfile parse(String encoded) throws Exception {
		UserProfile u = new UserProfile();
		for (String kv : encoded.split("&")) {
			String[] pair = kv.split("=");
			if (pair.length != 2) {
				throw new Exception("Invalid argument: " + kv);
			}
			u.put(pair[0], pair[1]);
		}
		return u;
	}
}
