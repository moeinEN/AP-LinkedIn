package Controller;

public class InputStringValidator {

    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final String USERNAME_REGEX = "^[\\w]+$"; // Matches English and Arabic letters

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

    public static boolean validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 20) {
            return false;
        }
        return name.matches("^[\\p{L}ا-ي]+$"); // Matches English and Arabic letters only
    }

    public static boolean validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty() || lastName.length() > 40) {
            return false;
        }
        return validateName(lastName); // Reusing validateName for letter check
    }

    public static boolean validateSize(int n, String input) {
        if (input == null || input.isEmpty() || input.length() > n) {
            return false;
        }
        return true;
    }


}
