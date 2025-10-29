package com.studentscores.student_score_management.repository;

import com.studentscores.student_score_management.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Student> searchByName(@Param("name") String name, Pageable pageable);
    
    boolean existsByStudentId(String studentId);
}