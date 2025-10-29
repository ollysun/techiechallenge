package com.studentscores.student_score_management.services;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StatisticsCalculator {
    
    public double calculateMean(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        return scores.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
    
    public double calculateMedian(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        
        List<Integer> sortedScores = new ArrayList<>(scores);
        Collections.sort(sortedScores);
        
        int size = sortedScores.size();
        if (size % 2 == 0) {
            int mid = size / 2;
            return (sortedScores.get(mid - 1) + sortedScores.get(mid)) / 2.0;
        } else {
            return sortedScores.get(size / 2);
        }
    }
    
    public Integer calculateMode(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        
        Map<Integer, Long> frequencyMap = scores.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        long maxFrequency = frequencyMap.values().stream()
                .max(Long::compare)
                .orElse(0L);
        
        if (maxFrequency <= 1) {
            return null; // No mode if all frequencies are 1
        }
        
        return frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .min(Integer::compareTo) // Return smallest mode in case of multiple modes
                .orElse(null);
    }
}