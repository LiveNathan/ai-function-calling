package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.UI;
import dev.nathanlively.adapter.in.web.droidcomm.KaribuTest;
import dev.nathanlively.security.Role;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}