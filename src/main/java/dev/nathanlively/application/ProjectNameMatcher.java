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

    public static Optional<String> from(String userInput, List<String> allNamesFromRepo) {
        String normalizedUserInput = normalize(userInput);

        return findExactMatch(allNamesFromRepo, normalizedUserInput).or(() -> findBestFuzzyMatch(allNamesFromRepo, normalizedUserInput));
    }

    private static Optional<String> findExactMatch(List<String> allNames, String normalizedUserInput) {
        if (allNames.contains(normalizedUserInput)) {
            return allNames.stream()
                    .filter(name -> name.equalsIgnoreCase(normalizedUserInput))
                    .findFirst();
        }
        return Optional.empty();
    }

    private static Optional<String> findBestFuzzyMatch(List<String> allNames, String normalizedUserInput) {
        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        String bestMatch = null;
        int highestScore = 0;

        for (String project : allNames) {
            int score = fuzzyScore.fuzzyScore(project, normalizedUserInput);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = project;
            }
        }

        int lengthBasedThreshold = calculateDynamicThreshold(normalizedUserInput);
        if (highestScore >= lengthBasedThreshold && bestMatch != null) {
            String finalBestMatch = bestMatch;
            return allNames.stream()
                    .filter(name -> name.equalsIgnoreCase(finalBestMatch))
                    .findFirst();
        }

        return Optional.empty();
    }

    private static String normalize(String input) {
        return input.trim().toLowerCase();
    }

    private static int calculateDynamicThreshold(String userInput) {
        int wordCount = userInput.split("\\s+").length;
        int upperThreshold = 17;
        int lowerThreshold = 11 + wordCount * 2;
        return Math.min(upperThreshold, lowerThreshold);
    }
}
