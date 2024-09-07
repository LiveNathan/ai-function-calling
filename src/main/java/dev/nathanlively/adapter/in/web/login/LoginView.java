package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.nathanlively.adapter.in.web.register.FormPurpose;
import dev.nathanlively.application.UserRegistrationService;
import dev.nathanlively.security.AuthenticatedUser;
import org.jetbrains.annotations.NotNull;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final UserRegistrationService service;

    public LoginView(AuthenticatedUser authenticatedUser, UserRegistrationService service) {
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
    private  Div createDiv() {
        TabSheet tabSheet = createTabSheet();
        return new Div(tabSheet);
    }

    @NotNull
    private  TabSheet createTabSheet() {
        FormLayout loginForm = createForm(FormPurpose.LOGIN);
        FormLayout registerForm = createForm(FormPurpose.REGISTER);

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Login", loginForm);
        tabSheet.add("Register", registerForm);
        return tabSheet;
    }

    @NotNull
    private FormLayout createForm(FormPurpose formPurpose) {
        EmailField validEmailField = createEmailField();
        PasswordField passwordField = createPasswordField();
        TextField nameField = createNameField();
//        Binder<Person> binder = new Binder<>(Person.class);
        Button submitButton = createButton(formPurpose, validEmailField, passwordField);

        FormLayout formLayout = new FormLayout();
        switch (formPurpose) {
            case LOGIN -> formLayout.add(validEmailField, passwordField, submitButton);
            case REGISTER -> formLayout.add(validEmailField, passwordField, nameField, submitButton);
        }
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        return formLayout;
    }

    @NotNull
    private Button createButton(FormPurpose formPurpose, EmailField emailField, PasswordField passwordField) {
        Button submitButton = new Button();
        switch (formPurpose) {
            case LOGIN -> {
                submitButton.setText("Login");
                submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                submitButton.addClickListener(event -> handleLogin(event, emailField, passwordField));
            }
            case REGISTER -> {
                submitButton.setText("Register");
                submitButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                submitButton.addClickListener(event -> handleRegistration(event, emailField, passwordField));
            }
        }
        return submitButton;
    }

    private void handleRegistration(ClickEvent<Button> event, EmailField emailField, PasswordField passwordField) {

    }

    private void handleLogin(ClickEvent<Button> event, EmailField emailField, PasswordField passwordField) {
        String email = emailField.getValue();
        String password = passwordField.getValue();
//        service.login

        // Handle registration logic
//        if (service.register(email, password)) {
        // Redirect to the main view after successful registration
//            UI.getCurrent().navigate("");
//        } else {
        // Show registration error
//        }
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
