package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.nathanlively.application.Result;
import dev.nathanlively.application.UserRegistrationService;
import dev.nathanlively.security.AuthenticatedUser;
import dev.nathanlively.security.User;

@AnonymousAllowed
@PageTitle("Register")
@Route(value = "register")
public class RegisterView extends LoginOverlay implements BeforeEnterObserver {
    private final AuthenticatedUser authenticatedUser;
    private final UserRegistrationService service;

    public RegisterView(AuthenticatedUser authenticatedUser, UserRegistrationService service) {
        this.authenticatedUser = authenticatedUser;
        this.service = service;

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("AI Function Calling");
        i18n.getHeader().setDescription("Register a new user");
        i18n.getForm().setSubmit("Register");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);
        setForgotPasswordButtonVisible(false);

        Button loginButton = new Button("Login Existing User", event ->
                getUI().ifPresent(ui -> ui.navigate("login"))
        );
        getCustomFormArea().add(loginButton);

        setOpened(true);

        addLoginListener(this::handleRegistration);
    }

    private void handleRegistration(LoginEvent event) {
        String username = event.getUsername();
        String password = event.getPassword();

        Result<User> result = service.with(username, password);
        if (result.isSuccess()) {
            Notification notification = Notification.show("Registration successful!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            getUI().ifPresent(ui -> ui.navigate("login"));
        } else {
            // Registration failed - show error
            String allFailureMessages = String.join(", ", result.failureMessages());
            showErrorMessage("Registration Failed", allFailureMessages);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }
        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
