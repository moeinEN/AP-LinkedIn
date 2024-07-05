package Model.Requests;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginCredentials {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
}
