package com.studentscores.student_score_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentReportDTO {
    private Long studentId;
    private String studentName;
    private String studentCode;
    private Map<String, Integer> subjectScores;
    private Double meanScore;
    private Double medianScore;
    private Integer modeScore;


    public StudentReportDTO(Long studentId, String studentName, String studentCode) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
    }

}