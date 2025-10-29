package com.studentscores.student_score_management.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubjectScoreDTO {
    private Long id;
    private String subjectName;
    private Integer score;


    public SubjectScoreDTO(Long id, String subjectName, Integer score) {
        this.id = id;
        this.subjectName = subjectName;
        this.score = score;
    }

}