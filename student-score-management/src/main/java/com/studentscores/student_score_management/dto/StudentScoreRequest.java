package com.studentscores.student_score_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentScoreRequest {
    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotNull(message = "Scores are required")
    @Size(min = 5, max = 5, message = "Exactly 5 subjects are required")
    private Map<@NotBlank String, @NotNull Integer> scores;


}