// StatisticsCalculatorTest.java
package com.studentscores.student_score_management.service;

import com.studentscores.student_score_management.services.StatisticsCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatisticsCalculatorTest {
    
    private StatisticsCalculator statisticsCalculator;
    
    @BeforeEach
    void setUp() {
        statisticsCalculator = new StatisticsCalculator();
    }
    
    @Test
    void calculateMean_ValidScores_ShouldReturnCorrectMean() {
        List<Integer> scores = Arrays.asList(85, 90, 78, 88, 92);
        double result = statisticsCalculator.calculateMean(scores);
        
        assertEquals(86.6, result, 0.01);
    }
    
    @Test
    void calculateMean_EmptyList_ShouldReturnZero() {
        List<Integer> scores = Collections.emptyList();
        double result = statisticsCalculator.calculateMean(scores);
        
        assertEquals(0.0, result);
    }
    
    @Test
    void calculateMedian_OddNumberOfScores_ShouldReturnCorrectMedian() {
        List<Integer> scores = Arrays.asList(85, 90, 78, 88, 92);
        double result = statisticsCalculator.calculateMedian(scores);
        
        assertEquals(88.0, result);
    }
    
    @Test
    void calculateMedian_EvenNumberOfScores_ShouldReturnCorrectMedian() {
        List<Integer> scores = Arrays.asList(85, 90, 78, 88);
        double result = statisticsCalculator.calculateMedian(scores);
        
        assertEquals(86.5, result);
    }
    
    @Test
    void calculateMedian_EmptyList_ShouldReturnZero() {
        List<Integer> scores = Collections.emptyList();
        double result = statisticsCalculator.calculateMedian(scores);
        
        assertEquals(0.0, result);
    }
    
    @Test
    void calculateMode_SingleMode_ShouldReturnCorrectMode() {
        List<Integer> scores = Arrays.asList(85, 90, 85, 88, 92);
        Integer result = statisticsCalculator.calculateMode(scores);
        
        assertEquals(85, result);
    }
    
    @Test
    void calculateMode_MultipleModes_ShouldReturnSmallestMode() {
        List<Integer> scores = Arrays.asList(85, 90, 85, 90, 92);
        Integer result = statisticsCalculator.calculateMode(scores);
        
        assertEquals(85, result);
    }
    
    @Test
    void calculateMode_NoMode_ShouldReturnNull() {
        List<Integer> scores = Arrays.asList(85, 90, 78, 88, 92);
        Integer result = statisticsCalculator.calculateMode(scores);
        
        assertNull(result);
    }
    
    @Test
    void calculateMode_EmptyList_ShouldReturnNull() {
        List<Integer> scores = Collections.emptyList();
        Integer result = statisticsCalculator.calculateMode(scores);
        
        assertNull(result);
    }
}