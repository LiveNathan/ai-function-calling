package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import dev.nathanlively.adapter.in.web.droidcomm.KaribuTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.github.mvysny.kaributesting.v10.LoginFormKt._login;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class LoginViewTest extends KaribuTest {
    @Test
    void nathanCanLogin() throws Exception {
        UI.getCurrent().navigate(LoginView.class);

        try {
            _login(_get(LoginOverlay.class), "not.existing@user.com", "pass");
        } catch (IllegalStateException e) {
            // From GoogleAnalyticsTracker. Ignore
        }

        assertThat(_get(LoginOverlay.class).getElement().getOuterHTML()).isEqualTo("<vaadin-login-overlay></vaadin-login-overlay>");
    }
}