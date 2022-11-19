package study.board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDto {

    private Long id;
    private String title;
    private String content;

    public BoardDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
