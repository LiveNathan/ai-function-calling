package dev.nathanlively.adapter.in.web.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import dev.nathanlively.adapter.in.web.MainLayout;
import dev.nathanlively.application.UnfulfilledRequestService;
import dev.nathanlively.domain.UnfulfilledUserRequest;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends Composite<VerticalLayout> {

    private final UnfulfilledRequestService service;

    public HomeView(UnfulfilledRequestService service) {
        this.service = service;
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Grid<UnfulfilledUserRequest> basicGrid = new Grid<>(UnfulfilledUserRequest.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(basicGrid);
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutColumn2.add(basicGrid);
    }

    private void setGridSampleData(Grid<UnfulfilledUserRequest> grid) {
        List<UnfulfilledUserRequest> unfulfilledUserRequests = service.findAll();
        grid.setItems(unfulfilledUserRequests);
    }
}
