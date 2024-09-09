package dev.nathanlively.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResourcesTest {

    @Test
    void timesheetEntriesPerProjectAreUpdated() throws Exception {
        Resources resources = new Resources();
        Project project = Project.create("Project A");
        List<TimesheetEntry> expected = List.of();

        List<TimesheetEntry> actual = resources.timesheetEntries(project);

        assertThat(actual)
                .isEqualTo(expected);
    }

}