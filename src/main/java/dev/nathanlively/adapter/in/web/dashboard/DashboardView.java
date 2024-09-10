package dev.nathanlively.adapter.in.web.dashboard;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.nathanlively.adapter.in.web.MainLayout;
import dev.nathanlively.application.TimesheetEntriesPerProject;
import dev.nathanlively.domain.TimesheetEntry;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@PermitAll
public class DashboardView extends Main {
    private final TimesheetEntriesPerProject timesheetEntriesByProject;
    private static final Logger log = LoggerFactory.getLogger(DashboardView.class);

    public DashboardView(TimesheetEntriesPerProject timesheetEntriesByProject) {
        this.timesheetEntriesByProject = timesheetEntriesByProject;
        addClassName("dashboard-view");
        UI ui = UI.getCurrent();
        initializeUIComponents(ui);
    }

    private void initializeUIComponents(UI ui) {
        H2 h2 = new H2("Timesheet Entries Per Project");
        Select<String> projectSelect = createProjectSelect();
        Grid<TimesheetEntry> entryGrid = createEntryGrid();

        ui.getPage().retrieveExtendedClientDetails(details -> {
            ZoneId finalZoneId = getZoneIdFromDetails(details.getTimeZoneId());
            Notification.show("ZoneId : " + finalZoneId.getId());
            configureEntryGrid(entryGrid, finalZoneId);
            updateProjectSelectOnChange(projectSelect, entryGrid);
        });

        Div timesheetEntriesDiv = createTimesheetEntriesDiv(h2, projectSelect, entryGrid);
        add(timesheetEntriesDiv);
    }

    private Select<String> createProjectSelect() {
        Select<String> projectSelect = new Select<>();
        projectSelect.setLabel("Projects");
        projectSelect.setItems(timesheetEntriesByProject.allProjectNames());
        return projectSelect;
    }

    private Grid<TimesheetEntry> createEntryGrid() {
        Grid<TimesheetEntry> entryGrid = new Grid<>(TimesheetEntry.class, false);
        entryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        return entryGrid;
    }

    private ZoneId getZoneIdFromDetails(String timeZoneId) {
        try {
            return ZoneId.of(timeZoneId);
        } catch (Exception e) {
            log.error("Invalid timezone ID: {}", timeZoneId, e);
            return ZoneId.systemDefault();
        }
    }

    private void configureEntryGrid(Grid<TimesheetEntry> entryGrid, ZoneId finalZoneId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        entryGrid.addColumn(entry -> entry.project().name()).setHeader("Project");
        entryGrid.addColumn(entry -> formatInstant(entry.workPeriod().start(), finalZoneId, formatter)).setHeader("Start");
        entryGrid.addColumn(entry -> formatInstant(entry.workPeriod().end(), finalZoneId, formatter)).setHeader("End");
        entryGrid.addColumn(entry -> entry.duration().toHours()).setHeader("Duration (HH)");
    }

    private String formatInstant(Instant instant, ZoneId zoneId, DateTimeFormatter formatter) {
        return instant.atZone(zoneId).format(formatter);
    }

    private void updateProjectSelectOnChange(Select<String> projectSelect, Grid<TimesheetEntry> entryGrid) {
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
    }

    private Div createTimesheetEntriesDiv(H2 h2, Select<String> projectSelect, Grid<TimesheetEntry> entryGrid) {
        Div timesheetEntriesDiv = new Div();
        timesheetEntriesDiv.getElement().getClassList().add("m-8");
        timesheetEntriesDiv.add(h2, projectSelect, entryGrid);
        return timesheetEntriesDiv;
    }
}
