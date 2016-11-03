package authentication;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptDecrypt {

	public static String generatePasswordHash(String userPassword) {

		return BCrypt.hashpw(userPassword, BCrypt.gensalt());
	}

	public static boolean isPasswordSame(String userPassword, String encryptedPassword) {
		if (userPassword == null) {
			return false;
		}
		if (encryptedPassword == null) {
			return false;
		}
		return BCrypt.checkpw(userPassword, encryptedPassword);
	}

}