package dev.nathanlively.adapter.in.web.dashboard;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.nathanlively.adapter.in.web.MainLayout;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.TimesheetEntry;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@PermitAll
public class DashboardView extends Main {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public DashboardView(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
        addClassName("dashboard-view");
        Select<String> select = new Select<>();
        select.setLabel("Projects");
        select.setItems(projectRepository.findAllNames());
        // Get selected project
        //

        Grid<TimesheetEntry> entryGrid = new Grid<>(TimesheetEntry.class, true);
        List<TimesheetEntry> entries = resourceRepository.timesheetEntriesByProject(null);
        entryGrid.setItems();
        add(select, entryGrid);
    }


}
