package com.studentscores.student_score_management.service;


import com.studentscores.student_score_management.dto.StudentScoreRequest;
import com.studentscores.student_score_management.entity.Student;
import com.studentscores.student_score_management.entity.SubjectScore;
import com.studentscores.student_score_management.exception.ResourceNotFoundException;
import com.studentscores.student_score_management.exception.ValidationException;
import com.studentscores.student_score_management.repository.StudentRepository;
import com.studentscores.student_score_management.repository.SubjectScoreRepository;
import com.studentscores.student_score_management.services.StatisticsCalculator;
import com.studentscores.student_score_management.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private SubjectScoreRepository subjectScoreRepository;
    
    @Mock
    private StatisticsCalculator statisticsCalculator;
    
    @InjectMocks
    private StudentService studentService;
    
    private StudentScoreRequest validRequest;
    private Student student;
    private List<SubjectScore> subjectScores;
    
    @BeforeEach
    void setUp() {
        validRequest = new StudentScoreRequest();
        validRequest.setStudentName("John Doe");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Math", 85);
        scores.put("Science", 90);
        scores.put("English", 78);
        scores.put("History", 88);
        scores.put("Geography", 92);
        validRequest.setScores(scores);
        
        student = new Student("John Doe");
        student.setId(1L);
        student.setStudentId("STU000001");
        
        subjectScores = scores.entrySet().stream()
                .map(entry -> new SubjectScore(student, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        student.setScores(subjectScores);
    }
    
    @Test
    void createStudentWithScores_ValidRequest_ShouldCreateStudentAndScores() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(subjectScoreRepository.saveAll(any())).thenReturn(subjectScores);
        
        Student result = studentService.createStudentWithScores(validRequest);
        
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(subjectScoreRepository, times(1)).saveAll(any());
    }
    
    @Test
    void createStudentWithScores_InvalidScoresCount_ShouldThrowValidationException() {
        Map<String, Integer> invalidScores = new HashMap<>();
        invalidScores.put("Math", 85);
        invalidScores.put("Science", 90);
        validRequest.setScores(invalidScores);
        
        ValidationException exception = assertThrows(ValidationException.class,
            () -> studentService.createStudentWithScores(validRequest));
        
        assertEquals("Exactly 5 subjects are required", exception.getMessage());
    }
    
    @Test
    void createStudentWithScores_InvalidScoreValue_ShouldThrowValidationException() {
        Map<String, Integer> invalidScores = new HashMap<>();
        invalidScores.put("Math", 85);
        invalidScores.put("Science", 90);
        invalidScores.put("English", 78);
        invalidScores.put("History", 88);
        invalidScores.put("Geography", 101); // Invalid score
        
        validRequest.setScores(invalidScores);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> studentService.createStudentWithScores(validRequest));
        
        assertTrue(exception.getMessage().contains("must be between 0 and 100"));
    }
    
    @Test
    void getStudentById_ExistingId_ShouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        
        Student result = studentService.getStudentById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
    }
    
    @Test
    void getStudentById_NonExistingId_ShouldThrowResourceNotFoundException() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());
        
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> studentService.getStudentById(999L));
        
        assertEquals("Student not found with id: 999", exception.getMessage());
    }
    
    @Test
    void generatePaginatedReports_ShouldReturnPaginatedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentPage = new PageImpl<>(List.of(student));
        
        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(subjectScoreRepository.findByStudentId(1L)).thenReturn(subjectScores);
        when(statisticsCalculator.calculateMean(any())).thenReturn(86.6);
        when(statisticsCalculator.calculateMedian(any())).thenReturn(88.0);
        when(statisticsCalculator.calculateMode(any())).thenReturn(85);
        
        var result = studentService.generatePaginatedReports(pageable, null);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(86.6, result.getContent().get(0).getMeanScore());
        assertEquals(88.0, result.getContent().get(0).getMedianScore());
        assertEquals(85, result.getContent().get(0).getModeScore());
    }
    
    @Test
    void deleteStudent_ExistingId_ShouldDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        
        studentService.deleteStudent(1L);
        
        verify(studentRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deleteStudent_NonExistingId_ShouldThrowResourceNotFoundException() {
        when(studentRepository.existsById(999L)).thenReturn(false);
        
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> studentService.deleteStudent(999L));
        
        assertEquals("Student not found with id: 999", exception.getMessage());
    }
}