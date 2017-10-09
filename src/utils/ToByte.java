package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ToByte {

	public static byte[] readBase64File(Path p) throws IOException {
		return Base64.getDecoder().decode(String.join("", Files.readAllLines(p)));
	}

	public static byte[] base64Decode(String in) {
		return Base64.getDecoder().decode(in);
	}

	public static byte[] hexDecode(String str) {
		byte[] b = new byte[(int) Math.ceil(str.length() / 2)];
		for (int i = 0; i < str.length(); i += 2) {
			b[i / 2] = (byte) Integer.parseInt(str.substring(i, i + 2), 16);
		}
		return b;
	}

}
