
package com.example.elearningplatform.course.section.lesson.question;

import java.time.LocalDateTime;

import com.example.elearningplatform.user.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.ToString;

@Data
@MappedSuperclass
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

  

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    User user;


    public Question(CreateQuestion createQuestion, User user) {
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
        this.user = user;
        this.content = createQuestion.getContent();

    }

    public Question() {
    }

}
