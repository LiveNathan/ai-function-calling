package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
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
        validEmailField.setLabel("Email address");
        validEmailField.setErrorMessage("Enter a valid email address");
        validEmailField.setClearButtonVisible(true);
        return validEmailField;
    }

    private void centerContent() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    @NotNull
    private Div createDiv() {
        TabSheet tabSheet = createTabSheet();
        return new Div(tabSheet);
    }

    @NotNull
    private TabSheet createTabSheet() {
        FormLayout loginForm = createForm(FormPurpose.LOGIN);
        FormLayout registerForm = createForm(FormPurpose.REGISTER);

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Login", loginForm);
        tabSheet.add("Register", registerForm);
        return tabSheet;
    }

    private static void notifyValidationException(ValidationException e) {
        Notification notification = Notification.show("Validation error: " + e.getMessage());
    }

    @NotNull
    private FormLayout createForm(FormPurpose formPurpose) {
        EmailField validEmailField = createEmailField();
        PasswordField passwordField = createPasswordField();
        TextField nameField = createNameField();

        Binder<UserDto> binder = new Binder<>(UserDto.class);
        binder.forField(validEmailField).asRequired("Email is required").bind(UserDto::getUsername, UserDto::setUsername);
        binder.forField(passwordField).asRequired("Password is required").bind(UserDto::getPassword, UserDto::setHashedPassword);
        if (formPurpose == FormPurpose.REGISTER) {
            binder.forField(nameField).asRequired("Name is required").bind(UserDto::getName, UserDto::setName);
        }
        Button submitButton = createButton(formPurpose, binder);

        FormLayout formLayout = new FormLayout();
        switch (formPurpose) {
            case LOGIN -> formLayout.add(validEmailField, passwordField, submitButton);
            case REGISTER -> formLayout.add(validEmailField, passwordField, nameField, submitButton);
        }
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        return formLayout;
    }

    @NotNull
    private Button createButton(FormPurpose formPurpose, Binder<UserDto> binder) {
        Button submitButton = new Button();
        switch (formPurpose) {
            case LOGIN -> {
                submitButton.setText("Login");
                submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                submitButton.addClickListener(event -> handleLogin(event, binder));
            }
            case REGISTER -> {
                submitButton.setText("Register");
                submitButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                submitButton.addClickListener(event -> handleRegistration(event, binder));
            }
        }
        return submitButton;
    }

    private void handleRegistration(ClickEvent<Button> event, Binder<UserDto> binder) {
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
        } else {
            Notification.show("Registration failed: " + result.failureMessages());
        }
    }

    private void handleLogin(ClickEvent<Button> event, Binder<UserDto> binder) {
        UserDto userDto = new UserDto();
        try {
            binder.writeBean(userDto);
        } catch (ValidationException e) {
            notifyValidationException(e);
            return;
        }
        Result<User> result = service.login(userDto);
        if (result.isSuccess()) {
//            authenticatedUser.set(result.getData());
            UI.getCurrent().getPage().setLocation("/");
        } else {
            Notification.show("Login failed: " + result.failureMessages());
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
