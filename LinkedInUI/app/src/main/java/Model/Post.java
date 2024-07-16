package Model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
    private String text;
    private Like likes;
    private Comment comments;
    private String mediaName;
    private int identification;
    private MiniProfile miniProfile;
    List<String> hashtags = new ArrayList<>();
    private MiniProfile miniProfile;
}
