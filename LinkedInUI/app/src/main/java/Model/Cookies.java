package Model;

public class Cookies {
    private static String sessionToken;
    private static String profileIdentification;

    public static void setSessionToken(String sessionToken) {
        Cookies.sessionToken = sessionToken;
    }

    public static String getSessionToken() {
        return sessionToken;
    }

    public static String getProfileIdentification() {
        return profileIdentification;
    }

    public static void setProfileIdentification(String profileIdentification) {
        Cookies.profileIdentification = profileIdentification;
    }
}
