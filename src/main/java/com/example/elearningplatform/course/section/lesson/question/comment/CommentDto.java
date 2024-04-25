package com.example.elearningplatform.course.section.lesson.question.comment;
import com.example.elearningplatform.course.section.lesson.question.QuestionDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto extends QuestionDto {

    private Integer numberOfReplyes = 0;
    private Integer numberOfLikes = 0;

    public CommentDto(Comment comment, Boolean isVotedByUser, Boolean isCreatedByUser) {
        super(comment, isVotedByUser, isCreatedByUser);
        if (comment == null)
            return;
        this.numberOfReplyes = comment.getNumberOfReplies();

    }

}
