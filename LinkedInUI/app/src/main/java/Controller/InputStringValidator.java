package Controller;

public class InputStringValidator {

    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final String USERNAME_REGEX = "^[\\p{L}ا-ي]+$"; // Matches English and Arabic letters

    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.isEmpty() || password.length() < 8) {
            return false;
        }
        boolean hasLetters = false;
        boolean hasNumbers = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetters = true;
            } else if (Character.isDigit(c)) {
                hasNumbers = true;
            }
        }
        return hasLetters && hasNumbers;
    }

    public static boolean validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.matches(USERNAME_REGEX);
    }


}
