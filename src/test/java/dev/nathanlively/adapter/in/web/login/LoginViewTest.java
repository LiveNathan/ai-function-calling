package dev.nathanlively.adapter.in.web.login;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import dev.nathanlively.adapter.in.web.droidcomm.KaribuTest;
import dev.nathanlively.security.Role;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class LoginViewTest extends KaribuTest {
    @Test
    void nathanCanLogin() throws Exception {
        login("nathanlively@gmail.com", "", List.of(String.valueOf(Role.ADMIN)));

        UI.getCurrent().navigate(LoginView.class);

        String pageTitle = UI.getCurrent().getInternals().getTitle();
        assertThat(pageTitle).isEqualTo("Home");
    }

    @Test
    @Disabled("until I learn how to test form layouts")
    void newUserCanViewRegisterForm() throws Exception {
        UI.getCurrent().navigate(LoginView.class);

        // Need to figure out how to click on the tab.
        _get(Tab.class, spec -> spec.withLabel("Register")).setSelected(true);
        awaitUIUpdates();
        assertThat(_get(Tab.class, spec -> spec.withLabel("Register")).isSelected())
                .isTrue();
//        assertThat(_get(FormLayout.class, spec -> spec.withId("register-form")).isVisible())
//                .isTrue();
        _get(EmailField.class, spec -> spec.withId("email-field-register")).setValue("travsi@micework.ch");
    }

    @Test
    @Disabled("until I learn how to test login forms")
    void nathanCanLoginWithForm() throws Exception {
        UI.getCurrent().navigate(LoginView.class);
        awaitUIUpdates();
        _get(TextField.class, spec -> spec.withId("vaadinLoginUsername")).setValue("nathanlively@gmail.com");
        _get(PasswordField.class, spec -> spec.withId("vaadinLoginPassword")).setValue("password");
        _get(Button.class, spec -> spec.withText("Log in")).click();

        String pageTitle = UI.getCurrent().getInternals().getTitle();
        assertThat(pageTitle).isEqualTo("Home");
    }

    private void awaitUIUpdates() {
        MockVaadin.clientRoundtrip();
    }
}