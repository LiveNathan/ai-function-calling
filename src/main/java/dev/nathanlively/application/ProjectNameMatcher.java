package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import org.apache.commons.text.similarity.FuzzyScore;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ProjectNameMatcher {
    private final ProjectRepository projectRepository;

    public ProjectNameMatcher(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Optional<String> from(String userInput) {
        List<String> allNames = projectRepository.findAllNames();
        // provide early return for exact match
        if (allNames.contains(userInput)) {
            return allNames.stream().filter(name -> name.equals(userInput)).findFirst();
        }

        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        String bestMatch = null;
        int highestScore = 0;

        // Iterate over available project names to find the best match
        for (String project : allNames) {
            int score = fuzzyScore.fuzzyScore(project, userInput);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = project;
            }
        }
        // Define your threshold for acceptance; adjust as needed based on testing
        int threshold = 5; // Adjust based on acceptable similarity scores

        // Return the best match if it meets the threshold, otherwise reject
        if (highestScore >= threshold) {
            return Optional.ofNullable(bestMatch);
        } else {
            return Optional.empty();
        }

    }
}
