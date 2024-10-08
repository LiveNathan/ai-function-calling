package dev.nathanlively.application;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectNameMatcherTest {
    static Stream<Arguments> provideProjectNames() {
        return Stream.of(
                Arguments.of("Pella Fall Planning Conference", "Pella Fall Planning Conference"),  // 1
                Arguments.of("pella", "Pella Fall Planning Conference"),  // 2
                Arguments.of("fall planin conf", "Pella Fall Planning Conference"),  // 3
                Arguments.of("MSF Fall", "MSF Fall History & Heritage Meeting"), // 4
                Arguments.of("Deloitte", ""),  // 5
                Arguments.of("Pella History meet", ""),  // 6
                Arguments.of("MSF Hist&Herit Meeting", "MSF Fall History & Heritage Meeting"),  // 7
                Arguments.of("msf fall history & heritage meeting", "MSF Fall History & Heritage Meeting"), // 8
                Arguments.of(" Pella Fall Planning Conference ", "Pella Fall Planning Conference")  // 9
        );
    }

    @ParameterizedTest
    @MethodSource("provideProjectNames")
    void from(String userSubmittedProjectName, String expected) {
        String pellaFallPlanningConference = "Pella Fall Planning Conference";
        String msfMeetingName = "MSF Fall History & Heritage Meeting";
        String projectA = "Project A (12345)";
        List<String> allNamesFromRepo = List.of(pellaFallPlanningConference, msfMeetingName, projectA);

        Optional<String> actual = ProjectNameMatcher.from(userSubmittedProjectName, allNamesFromRepo);

        if (expected.isEmpty()) {
            assertThat(actual).isEmpty();
        } else {
            assertThat(actual).isEqualTo(Optional.of(expected));
        }
    }

}