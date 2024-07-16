package Model;

import lombok.Getter;
import lombok.Setter;


public class Cookies {
    private static String sessionToken;
    private static Integer profileId;

    public static String getSessionToken() {
        return sessionToken;
    }

    public static void setSessionToken(String sessionToken) {
        Cookies.sessionToken = sessionToken;
    }

    public static Integer getProfileId() {
        return profileId;
    }

    public static void setProfileId(Integer profileId) {
        Cookies.profileId = profileId;
    }
}
