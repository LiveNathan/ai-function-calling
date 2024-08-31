package dev.nathanlively.application;

import org.apache.commons.text.similarity.FuzzyScore;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectNameMatcher {

    public static Optional<String> from(String userInput, List<String> allNamesFromRepo) {
        String normalizedUserInput = normalize(userInput);
        Map<String, String> normalizedNamesMap = allNamesFromRepo.stream()
                .collect(Collectors.toMap(ProjectNameMatcher::normalize, Function.identity()));

        return findExactMatch(normalizedNamesMap, normalizedUserInput)
                .or(() -> findBestFuzzyMatch(normalizedNamesMap, normalizedUserInput));
    }

    private static Optional<String> findExactMatch(Map<String, String> normalizedNamesMap, String normalizedUserInput) {
        return Optional.ofNullable(normalizedNamesMap.get(normalizedUserInput));
    }

    private static Optional<String> findBestFuzzyMatch(Map<String, String> normalizedNamesMap, String normalizedUserInput) {
        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        String bestMatch = null;
        int highestScore = 0;

        for (String normalizedProject : normalizedNamesMap.keySet()) {
            int score = fuzzyScore.fuzzyScore(normalizedProject, normalizedUserInput);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = normalizedProject;
            }
        }

        int lengthBasedThreshold = calculateDynamicThreshold(normalizedUserInput);
        if (highestScore >= lengthBasedThreshold && bestMatch != null) {
            return Optional.ofNullable(normalizedNamesMap.get(bestMatch));
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
