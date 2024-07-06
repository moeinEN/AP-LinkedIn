package Model;

public enum JobWorkplaceStatus {
    ON_SITE("On-site"),
    HYBRID("Hybrid"),
    REMOTE("Remote");

    private String value;
    private JobWorkplaceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JobWorkplaceStatus fromValue(String value) {
        for (JobWorkplaceStatus status : JobWorkplaceStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return JobWorkplaceStatus.ON_SITE; // Default case if the value does not match any enum constant
    }
}
