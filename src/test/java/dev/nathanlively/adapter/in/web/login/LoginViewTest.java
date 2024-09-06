package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import dev.nathanlively.adapter.in.web.droidcomm.KaribuTest;
import dev.nathanlively.security.Role;
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
    void newUserCanRegister() throws Exception {
        _get(Button.class, spec -> spec.withText("Register New User")).click();

        String pageTitle = UI.getCurrent().getInternals().getTitle();
        assertThat(pageTitle).isEqualTo("Register");
    }
}