package dev.nathanlively.adapter.in.web.dashboard;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.nathanlively.adapter.in.web.MainLayout;
import dev.nathanlively.application.TimesheetEntriesPerProject;
import dev.nathanlively.domain.TimesheetEntry;
import jakarta.annotation.security.PermitAll;

import java.util.Collections;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@PermitAll
public class DashboardView extends Main {
    private final TimesheetEntriesPerProject timesheetEntriesByProject;

    public DashboardView(TimesheetEntriesPerProject timesheetEntriesByProject) {
        this.timesheetEntriesByProject = timesheetEntriesByProject;
        addClassName("dashboard-view");

        Select<String> projectSelect = new Select<>();
        projectSelect.setLabel("Projects");
        projectSelect.setItems(timesheetEntriesByProject.allProjectNames());

        Grid<TimesheetEntry> entryGrid = new Grid<>(TimesheetEntry.class, false);
        entryGrid.addColumn(entry -> entry.project().name()).setHeader("Project");
        entryGrid.addColumn(entry -> entry.duration().toHours()).setHeader("Duration");
        entryGrid.setAllRowsVisible(true);

        projectSelect.addValueChangeListener(event -> {
            String selectedProject = event.getValue();
            if (selectedProject != null) {
                List<TimesheetEntry> entries = timesheetEntriesByProject.with(selectedProject);
                entryGrid.setItems(entries);
            } else {
                entryGrid.setItems(Collections.emptyList());
                Notification.show("Empty List");
            }
        });

        add(projectSelect, entryGrid);
    }


}
