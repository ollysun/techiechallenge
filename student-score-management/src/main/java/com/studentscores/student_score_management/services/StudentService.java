// StudentService.java
package com.studentscores.student_score_management.services;


import com.studentscores.student_score_management.dto.PaginatedResponse;
import com.studentscores.student_score_management.dto.StudentReportDTO;
import com.studentscores.student_score_management.dto.StudentScoreRequest;
import com.studentscores.student_score_management.entity.Student;
import com.studentscores.student_score_management.entity.SubjectScore;
import com.studentscores.student_score_management.repository.StudentRepository;
import com.studentscores.student_score_management.repository.SubjectScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final SubjectScoreRepository subjectScoreRepository;
    private final StatisticsCalculator statisticsCalculator;
    
    @Autowired
    public StudentService(StudentRepository studentRepository, 
                         SubjectScoreRepository subjectScoreRepository,
                         StatisticsCalculator statisticsCalculator) {
        this.studentRepository = studentRepository;
        this.subjectScoreRepository = subjectScoreRepository;
        this.statisticsCalculator = statisticsCalculator;
    }
    
    public Student createStudentWithScores(StudentScoreRequest request) {
        validateScores(request.getScores());
        
        Student student = new Student(request.getStudentName());
        student = studentRepository.save(student);
        
        List<SubjectScore> subjectScores = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : request.getScores().entrySet()) {
            SubjectScore subjectScore = new SubjectScore(student, entry.getKey(), entry.getValue());
            subjectScores.add(subjectScore);
        }
        
        subjectScoreRepository.saveAll(subjectScores);
        student.setScores(subjectScores);
        
        return student;
    }
    
    private void validateScores(Map<String, Integer> scores) {
        if (scores == null || scores.size() != 5) {
            throw new ValidationException("Exactly 5 subjects are required");
        }
        
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty()) {
                throw new ValidationException("Subject name cannot be empty");
            }
            if (entry.getValue() == null || entry.getValue() < 0 || entry.getValue() > 100) {
                throw new ValidationException("Score for " + entry.getKey() + " must be between 0 and 100");
            }
        }
    }
    
    @Transactional(readOnly = true)
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Student> searchStudentsByName(String name, Pageable pageable) {
        return studentRepository.searchByName(name, pageable);
    }
    
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
    }
    
    @Transactional(readOnly = true)
    public List<StudentReportDTO> generateReports() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToReportDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PaginatedResponse<StudentReportDTO> generatePaginatedReports(Pageable pageable, String search) {
        Page<Student> studentPage;
        
        if (search != null && !search.trim().isEmpty()) {
            studentPage = studentRepository.searchByName(search, pageable);
        } else {
            studentPage = studentRepository.findAll(pageable);
        }
        
        List<StudentReportDTO> reports = studentPage.getContent().stream()
                .map(this::convertToReportDTO)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(
            reports,
            studentPage.getNumber(),
            studentPage.getTotalPages(),
            studentPage.getTotalElements(),
            studentPage.getSize()
        );
    }
    
    private StudentReportDTO convertToReportDTO(Student student) {
        StudentReportDTO report = new StudentReportDTO(
            student.getId(), 
            student.getName(), 
            student.getStudentId()
        );
        
        List<SubjectScore> scores = subjectScoreRepository.findByStudentId(student.getId());
        Map<String, Integer> subjectScores = scores.stream()
                .collect(Collectors.toMap(
                    SubjectScore::getSubjectName, 
                    SubjectScore::getScore
                ));
        
        List<Integer> scoreValues = scores.stream()
                .map(SubjectScore::getScore)
                .collect(Collectors.toList());
        
        report.setSubjectScores(subjectScores);
        report.setMeanScore(statisticsCalculator.calculateMean(scoreValues));
        report.setMedianScore(statisticsCalculator.calculateMedian(scoreValues));
        report.setModeScore(statisticsCalculator.calculateMode(scoreValues));
        
        return report;
    }
    
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}