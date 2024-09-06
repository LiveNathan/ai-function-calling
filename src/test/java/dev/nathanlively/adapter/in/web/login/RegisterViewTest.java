package dev.nathanlively.adapter.in.web.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import dev.nathanlively.adapter.in.web.droidcomm.KaribuTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class RegisterViewTest extends KaribuTest {
    @Test
    void newUserCanRegister() throws Exception {
        _get(Button.class, spec -> spec.withText("Register New User")).click();

        assertThat(UI.getCurrent().getInternals().getTitle()).isEqualTo("Register");

        _get(TextField.class, spec -> spec.withLabel("Username")).setValue("Travis");
        _get(TextField.class, spec -> spec.withLabel("Password")).setValue("12345");
        _get(Button.class, spec -> spec.withText("Register")).click();

        assertThat(UI.getCurrent().getInternals().getTitle()).isEqualTo("Login");
    }
}