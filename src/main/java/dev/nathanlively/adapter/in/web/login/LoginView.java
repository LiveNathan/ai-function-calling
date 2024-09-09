package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.nathanlively.application.Result;
import dev.nathanlively.application.UserService;
import dev.nathanlively.security.AuthenticatedUser;
import dev.nathanlively.security.User;
import org.jetbrains.annotations.NotNull;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final UserService service;
    private final TabSheet tabSheet = createTabSheet();

    public LoginView(AuthenticatedUser authenticatedUser, UserService service) {
        this.authenticatedUser = authenticatedUser;
        this.service = service;
        Div tabSheetContainer = createDiv();
        centerContent();
        add(tabSheetContainer);
    }

    @NotNull
    private static TextField createNameField() {
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        return nameField;
    }

    @NotNull
    private static PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");
        return passwordField;
    }

    @NotNull
    private static EmailField createEmailField() {
        EmailField validEmailField = new EmailField();
        validEmailField.setLabel("Email");
        validEmailField.setErrorMessage("Enter a valid email address");
        validEmailField.setClearButtonVisible(true);
        validEmailField.setId("email-field-register");
        return validEmailField;
    }

    private void centerContent() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    @NotNull
    private Div createDiv() {
        return new Div(tabSheet);
    }

    private static void notifyValidationException(ValidationException e) {
        Notification.show("Validation error: " + e.getMessage());
    }

    @NotNull
    private TabSheet createTabSheet() {
        LoginForm loginForm = createLoginForm();
        FormLayout registerForm = createRegistrationForm();

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Login", loginForm);
        tabSheet.add("Register", registerForm);
        return tabSheet;
    }

    @NotNull
    private LoginForm createLoginForm() {
        LoginForm loginForm = new LoginForm();
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("");
        i18n.getForm().setUsername("Email");
        i18n.setAdditionalInformation(null);
        loginForm.setI18n(i18n);
        loginForm.setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        return loginForm;
    }

    @NotNull
    private FormLayout createRegistrationForm() {
        EmailField validEmailField = createEmailField();
        PasswordField passwordField = createPasswordField();
        TextField nameField = createNameField();

        Binder<UserDto> binder = new Binder<>(UserDto.class);
        binder.forField(validEmailField).asRequired("Email is required").bind(UserDto::getUsername, UserDto::setUsername);
        binder.forField(passwordField).asRequired("Password is required").bind(UserDto::getPassword, UserDto::setHashedPassword);
        binder.forField(nameField).asRequired("Name is required").bind(UserDto::getName, UserDto::setName);
        Button submitButton = createSubmitButton(binder);

        FormLayout formLayout = new FormLayout();
        formLayout.add(validEmailField, passwordField, nameField, submitButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setMaxWidth("360px");
        formLayout.setId("register-form");
        return formLayout;
    }

    @NotNull
    private Button createSubmitButton(Binder<UserDto> binder) {
        Button submitButton = new Button();
        submitButton.setText("Register");
        submitButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        submitButton.addClickListener(event -> handleRegistration(binder));
        return submitButton;
    }

    private void handleRegistration(Binder<UserDto> binder) {
        UserDto userDto = new UserDto();
        try {
            binder.writeBean(userDto);
        } catch (ValidationException e) {
            notifyValidationException(e);
            return;
        }
        Result<User> result = service.register(userDto);
        if (result.isSuccess()) {
            Notification.show("Registration successful");
            // set Login as active tab
            tabSheet.setSelectedIndex(0);
        } else {
            Notification.show("Registration failed: " + result.failureMessages());
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            event.forwardTo("");
        }

//        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
