package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {

	public static boolean isEmailValid(String email) {

		Pattern emailPattern = Pattern.compile(
				"^[a-zA-Z0-9_\\+-]+(\\.[a-zA-Z0-9_\\+-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.([a-zA-Z]{2,4})$");
		Matcher matcher = emailPattern.matcher(email);
		return matcher.matches();
	}

}