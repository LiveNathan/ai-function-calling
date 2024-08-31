package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import org.apache.commons.text.similarity.FuzzyScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ProjectNameMatcher {
    private static final Logger log = LoggerFactory.getLogger(ProjectNameMatcher.class);
    private final ProjectRepository projectRepository;

    public ProjectNameMatcher(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Optional<String> from(String userInput) {
        String normalizedUserInput = userInput.trim().toLowerCase();
        List<String> allNames = projectRepository.findAllNames().stream()
                .map(String::toLowerCase)
                .toList();

        // Provide early return for exact match after normalization
        if (allNames.contains(normalizedUserInput)) {
            return projectRepository.findAllNames().stream()
                    .filter(name -> name.equalsIgnoreCase(userInput.trim()))
                    .findFirst();
        }

        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        String bestMatch = null;
        int highestScore = 0;

        // Iterate over available project names to find the best match
        for (String project : allNames) {
            int score = fuzzyScore.fuzzyScore(project, normalizedUserInput);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = project;
            }
        }

        // Dynamic threshold based on length of user input
        int lengthBasedThreshold = calculateDynamicThreshold(normalizedUserInput);

        // Return the best match if it meets the dynamic threshold, otherwise reject
        if (highestScore >= lengthBasedThreshold) {
            String finalBestMatch = bestMatch;
            return projectRepository.findAllNames().stream()
                    .filter(name -> name.equalsIgnoreCase(finalBestMatch))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    // Method to calculate the dynamic threshold based on input length
    private int calculateDynamicThreshold(String userInput) {
        int wordCount = userInput.split("\\s+").length;
        // Example logic for dynamic threshold: from lower to upper threshold based on word count
        // Adjust this logic as needed
        int upperThreshold = 17;
        int lowerThreshold = 11 + wordCount * 2;
        log.info(String.valueOf(lowerThreshold));
        return Math.min(upperThreshold, lowerThreshold);
    }
}
