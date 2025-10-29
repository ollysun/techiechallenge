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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
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
                .andExpect(jsonPath("$.scores.length()").value(5))
                .andExpect(jsonPath("$.scores[0].subjectName").exists())
                .andExpect(jsonPath("$.scores[0].score").exists());
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

        // Create test data with duplicate scores to ensure a mode exists
        StudentScoreRequest requestWithMode = new StudentScoreRequest();
        requestWithMode.setStudentName("Jane Smith");
        Map<String, Integer> scoresWithMode = new HashMap<>();
        scoresWithMode.put("Math", 85);
        scoresWithMode.put("Science", 85); // Duplicate - this will create a mode
        scoresWithMode.put("English", 78);
        scoresWithMode.put("History", 88);
        scoresWithMode.put("Geography", 92);
        requestWithMode.setScores(scoresWithMode);


        // First create a student with scores that have a mode
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithMode)));

        mockMvc.perform(get("/api/students/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].studentName").value("Jane Smith"))
                .andExpect(jsonPath("$[0].meanScore").exists())
                .andExpect(jsonPath("$[0].medianScore").exists())
                .andExpect(jsonPath("$[0].modeScore").exists()) // Now modeScore should exist
                .andExpect(jsonPath("$[0].modeScore").value(85)) // And have the expected value
                .andExpect(jsonPath("$[0].subjectScores").isMap())
                .andExpect(jsonPath("$[0].subjectScores.Math").value(85));
    }

    @Test
    void generateReports_MultipleScenarios_ShouldCalculateStatisticsCorrectly() throws Exception {
        // Test Case 1: Student with no mode (all unique scores)
        StudentScoreRequest noModeRequest = new StudentScoreRequest();
        noModeRequest.setStudentName("No Mode Student");
        Map<String, Integer> noModeScores = Map.of(
                "Math", 85,
                "Science", 90,
                "English", 78,
                "History", 88,
                "Geography", 92
        );
        noModeRequest.setScores(noModeScores);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noModeRequest)));

        // Test Case 2: Student with a clear mode
        StudentScoreRequest withModeRequest = new StudentScoreRequest();
        withModeRequest.setStudentName("With Mode Student");
        Map<String, Integer> withModeScores = Map.of(
                "Math", 85,
                "Science", 85, // Duplicate
                "English", 78,
                "History", 88,
                "Geography", 92
        );
        withModeRequest.setScores(withModeScores);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withModeRequest)));

        // Test Case 3: Student with multiple modes
        StudentScoreRequest multiModeRequest = new StudentScoreRequest();
        multiModeRequest.setStudentName("Multi Mode Student");
        Map<String, Integer> multiModeScores = Map.of(
                "Math", 85,
                "Science", 85, // Duplicate
                "English", 90,
                "History", 90, // Another duplicate
                "Geography", 78
        );
        multiModeRequest.setScores(multiModeScores);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(multiModeRequest)));

        // Verify all reports
        mockMvc.perform(get("/api/students/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))

                // Student 1: No mode (all unique)
                .andExpect(jsonPath("$[0].studentName").value("No Mode Student"))
                .andExpect(jsonPath("$[0].meanScore").value(86.6))
                .andExpect(jsonPath("$[0].medianScore").value(88.0))
                .andExpect(jsonPath("$[0].modeScore").doesNotExist())

                // Student 2: Single mode
                .andExpect(jsonPath("$[1].studentName").value("With Mode Student"))
                .andExpect(jsonPath("$[1].meanScore").value(85.6))
                .andExpect(jsonPath("$[1].medianScore").value(85.0))
                .andExpect(jsonPath("$[1].modeScore").value(85))

                // Student 3: Multiple modes (should return smallest)
                .andExpect(jsonPath("$[2].studentName").value("Multi Mode Student"))
                .andExpect(jsonPath("$[2].meanScore").value(85.6))
                .andExpect(jsonPath("$[2].medianScore").value(85.0))
                .andExpect(jsonPath("$[2].modeScore").value(85));
    }

    @Test
    void generateReports_WithMode_ShouldReturnModeScore() throws Exception {
        // Test data with a clear mode
        StudentScoreRequest request = new StudentScoreRequest();
        request.setStudentName("Student With Mode");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Math", 85);
        scores.put("Science", 85); // Duplicate
        scores.put("English", 78);
        scores.put("History", 88);
        scores.put("Geography", 85); // Another duplicate - mode should be 85
        request.setScores(scores);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/students/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modeScore").exists())
                .andExpect(jsonPath("$[0].modeScore").value(85));
    }

    @Test
    void generateReports_WithoutMode_ShouldReturnDoesNotexistModeScores() throws Exception {
        // Test data with no mode (all unique scores)
        StudentScoreRequest request = new StudentScoreRequest();
        request.setStudentName("Student Without Mode");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Math", 85);
        scores.put("Science", 90);
        scores.put("English", 78);
        scores.put("History", 88);
        scores.put("Geography", 92); // All unique scores
        request.setScores(scores);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/students/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].studentName").value("Student Without Mode"))
                .andExpect(jsonPath("$[1].modeScore").doesNotExist()); // modeScore should be null for unique scores
    }

    @Test
    void generateReports_WithoutMode_ShouldReturnNullModeScore() throws Exception {
        // Test data with no mode (all unique scores)
        StudentScoreRequest request = new StudentScoreRequest();
        request.setStudentName("Student Without Mode");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Math", 85);
        scores.put("Science", 90);
        scores.put("English", 78);
        scores.put("History", 88);
        scores.put("Geography", 92);
        request.setScores(scores);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Use content() to search for the specific student by name
        mockMvc.perform(get("/api/students/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.studentName == 'Student Without Mode')].modeScore").isEmpty());
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