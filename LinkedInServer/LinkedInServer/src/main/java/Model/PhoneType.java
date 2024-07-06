package Model;

public enum PhoneType {
    MOBILE_PHONE("mobile-phone"),
    HOME_PHONE("home-phone"),
    WORK_PHONE("work-phone");


    private String value;
    private PhoneType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PhoneType fromValue(String value) {
        for (PhoneType status : PhoneType.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return PhoneType.MOBILE_PHONE; // Default case if the value does not match any enum constant
    }
}
