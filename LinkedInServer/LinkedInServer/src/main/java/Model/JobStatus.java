package Model;

import lombok.Getter;

@Getter
public enum JobStatus {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    SELF_EMPLOYED("Self-employed"),
    FREELANCE("Freelance"),
    CONTRACT("Contract"),
    INTERNSHIP("Internship"),
    PAID_INTERNSHIP("Paid Internship"),
    SEASONAL("Seasonal"),
    NULL("");

    private String value;
    private JobStatus(String value) {
        this.value = value;
    }

    public static JobStatus fromValue(String value) {
        for (JobStatus status : JobStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return JobStatus.NULL; // Default case if the value does not match any enum constant
    }
}
