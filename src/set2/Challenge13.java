package set2;

import java.util.Arrays;

import utils.Utils;

public class Challenge13 {

	private static byte[] wrapProfileFor(byte[] in) {
		return UserProfile.profileFor(new String(in));
	}

	public static void main(String[] args) {
		byte[] admin = Utils.addPkcs7Padding("admin".getBytes(), 16);
		for (int pre = 0; pre < 16; pre++) {
			for (int post = 0; post < 16; post++) {
				byte[] prefix = new byte[pre];
				Arrays.fill(prefix, (byte) 65);
				byte[] postfix = new byte[post];
				Arrays.fill(postfix, (byte) 65);
				byte[] test = new byte[pre + admin.length + post];
				System.arraycopy(prefix, 0, test, 0, prefix.length);
				System.arraycopy(admin, 0, test, prefix.length, admin.length);
				System.arraycopy(postfix, 0, test, prefix.length + admin.length, postfix.length);
				byte[] res = wrapProfileFor(test);

				for (int i = 0; i < 16; i++) {
					res[res.length - 16 + i] = res[i + 16];
				}
				try {
					UserProfile u = UserProfile.decryptAndParse(res);
					if (u.isAdmin()) {
						System.out.println(u.encode());
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

	}

}
