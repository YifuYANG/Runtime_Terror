package app.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ForumPostVO {

    private String userName;
    private String content;
    private Timestamp date;

}
