package Model;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileHeader {
    private String firstName;
    private String lastName;
    private String additionalName;
    private String mainImageUrl;
    private String backgroundImageUrl;
    private String about;
    private ProfileJob currentJob;
    private ProfileEducation educationalInfo;
    private String country;
    private String city;
    private String profession;
    private ProfileContactInfo contactInfo;
    private String jobStatus;
}
