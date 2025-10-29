package com.studentscores.student_score_management.controller;


import com.studentscores.student_score_management.dto.PaginatedResponse;
import com.studentscores.student_score_management.dto.StudentReportDTO;
import com.studentscores.student_score_management.dto.StudentScoreRequest;
import com.studentscores.student_score_management.entity.Student;
import com.studentscores.student_score_management.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Scores API", description = "API for managing student scores and generating reports")
public class StudentController {
    
    private final StudentService studentService;
    
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create a new student with scores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Parameter(description = "Student score data")
            @Valid @RequestBody StudentScoreRequest request) {
        Student student = studentService.createStudentWithScores(request);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Get all students with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<StudentReportDTO>> getAllStudents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Search by student name") @RequestParam(required = false) String search) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        PaginatedResponse<StudentReportDTO> response = studentService.generatePaginatedReports(pageable, search);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get student by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "Student ID") @PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }
    
    @Operation(summary = "Generate comprehensive reports for all students")
    @GetMapping("/reports")
    public ResponseEntity<List<StudentReportDTO>> generateReports() {
        List<StudentReportDTO> reports = studentService.generateReports();
        return ResponseEntity.ok(reports);
    }
    
    @Operation(summary = "Delete student by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID") @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}