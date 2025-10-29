// StudentControllerIntegrationTest.java
package com.studentscores.student_score_management.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentscores.student_score_management.dto.StudentScoreRequest;
import com.studentscores.student_score_management.entity.Student;
import com.studentscores.student_score_management.repository.StudentRepository;
import com.studentscores.student_score_management.repository.SubjectScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private SubjectScoreRepository subjectScoreRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private StudentScoreRequest validRequest;
    
    @BeforeEach
    void setUp() {
        subjectScoreRepository.deleteAll();
        studentRepository.deleteAll();
        
        validRequest = new StudentScoreRequest();
        validRequest.setStudentName("John Doe");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Math", 85);
        scores.put("Science", 90);
        scores.put("English", 78);
        scores.put("History", 88);
        scores.put("Geography", 92);
        validRequest.setScores(scores);
    }
    
    @Test
    void createStudent_ValidRequest_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.studentId").exists())
                .andExpect(jsonPath("$.scores").isArray())
                .andExpect(jsonPath("$.scores.length()").value(5));
    }
    
    @Test
    void createStudent_InvalidScore_ShouldReturnBadRequest() throws Exception {
        validRequest.getScores().put("Math", 101); // Invalid score
        
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void createStudent_InsufficientSubjects_ShouldReturnBadRequest() throws Exception {
        validRequest.getScores().remove("Geography"); // Only 4 subjects
        
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getAllStudents_ShouldReturnPaginatedResponse() throws Exception {
        // First create a student
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));
        
        mockMvc.perform(get("/api/students")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalItems").value(1));
    }
    
    @Test
    void generateReports_ShouldReturnReportsWithStatistics() throws Exception {
        // First create a student
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));
        
        mockMvc.perform(get("/api/students/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].studentName").value("John Doe"))
                .andExpect(jsonPath("$[0].meanScore").exists())
                .andExpect(jsonPath("$[0].medianScore").exists())
                .andExpect(jsonPath("$[0].modeScore").exists())
                .andExpect(jsonPath("$[0].subjectScores").isMap())
                .andExpect(jsonPath("$[0].subjectScores.Math").value(85));
    }
    
    @Test
    void deleteStudent_ExistingStudent_ShouldReturnNoContent() throws Exception {
        Student student = new Student("Test Student");
        student = studentRepository.save(student);
        
        mockMvc.perform(delete("/api/students/{id}", student.getId()))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deleteStudent_NonExistingStudent_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}