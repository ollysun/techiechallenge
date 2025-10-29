package com.studentscores.student_score_management.repository;

import com.studentscores.student_score_management.entity.SubjectScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectScoreRepository extends JpaRepository<SubjectScore, Long> {
    List<SubjectScore> findByStudentId(Long studentId);
    
    Optional<SubjectScore> findByStudentIdAndSubjectName(Long studentId, String subjectName);
    
    @Query("SELECT ss FROM SubjectScore ss WHERE ss.student.id = :studentId")
    List<SubjectScore> findScoresByStudentId(@Param("studentId") Long studentId);
    
    boolean existsByStudentIdAndSubjectName(Long studentId, String subjectName);
}