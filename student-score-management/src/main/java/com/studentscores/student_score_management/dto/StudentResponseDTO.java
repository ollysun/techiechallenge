package com.studentscores.student_score_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String studentId;
    private List<SubjectScoreDTO> scores;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public StudentResponseDTO(Long id, String name, String studentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}