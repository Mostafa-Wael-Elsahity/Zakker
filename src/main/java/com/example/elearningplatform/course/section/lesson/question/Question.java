
package com.example.elearningplatform.course.section.lesson.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.user.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

  

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
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
