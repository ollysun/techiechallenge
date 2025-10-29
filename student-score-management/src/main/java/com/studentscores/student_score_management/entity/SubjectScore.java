package com.studentscores.student_score_management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subject_scores", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_name"}))
public class SubjectScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference // This side won't be serialized
    private Student student;

    @NotNull(message = "Subject name is required")
    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be between 0 and 100")
    @Max(value = 100, message = "Score must be between 0 and 100")
    @Column(nullable = false)
    private Integer score;


    public SubjectScore(Student student, String subjectName, Integer score) {
        this.student = student;
        this.subjectName = subjectName;
        this.score = score;
    }


}