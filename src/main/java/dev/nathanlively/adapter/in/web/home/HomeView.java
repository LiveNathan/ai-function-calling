package dev.nathanlively.adapter.in.web.home;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import dev.nathanlively.adapter.in.web.MainLayout;
import dev.nathanlively.application.UnfulfilledRequestService;
import dev.nathanlively.domain.UnfulfilledUserRequest;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout {
    private final UnfulfilledRequestService service;
    private Grid<UnfulfilledUserRequest> basicGrid;

    @Autowired
    public HomeView(UnfulfilledRequestService service) {
        this.service = service;
        createBasicGrid();
        add(basicGrid);
    }

    private void createBasicGrid() {
        basicGrid = new Grid<>(UnfulfilledUserRequest.class, false);

        basicGrid.addColumn(UnfulfilledUserRequest::unfulfilledRequest).setHeader("Request");
        basicGrid.addColumn(UnfulfilledUserRequest::conversationId).setHeader("Conversation ID");

        basicGrid.setItemDetailsRenderer(new ComponentRenderer<>(UnfulfilledRequestDetailsFormLayout::new,
                UnfulfilledRequestDetailsFormLayout::setRequest));

        basicGrid.setItems(service.findAll());
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");
    }

    private static class UnfulfilledRequestDetailsFormLayout extends FormLayout {
        private final TextArea request = new TextArea("Request");
        private final TextField conversationId = new TextField("Conversation ID");

        public UnfulfilledRequestDetailsFormLayout() {
            request.setReadOnly(true);
            conversationId.setReadOnly(true);

            add(request, conversationId);
            setResponsiveSteps(new ResponsiveStep("0", 3));
            setColspan(request, 3);
            setColspan(conversationId, 3);
        }

        public void setRequest(UnfulfilledUserRequest request) {
            this.request.setValue(request.unfulfilledRequest());
            this.conversationId.setValue(request.conversationId());
        }
    }
}
