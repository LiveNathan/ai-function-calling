package dev.nathanlively.adapter.in.web.droidcomm;

import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.nathanlively.adapter.in.web.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@PageTitle("DroidComm")
@Route(value = "DroidComm", layout = MainLayout.class)
@PermitAll
public class DroidCommView extends VerticalLayout {
    private final ArrayList<Message> messages = new ArrayList<>();
    private final List<MessageListItem> items = new ArrayList<>();
    private final MessageList messageList = new MessageList(items);
    private final ChatClient chatClient;

    public DroidCommView(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();

        addClassNames("droid-comm-view", LumoUtility.Width.FULL, LumoUtility.Height.FULL, "display-flex", "flex-auto");
        setSizeFull();

        Scroller messageScroller = new Scroller(messageList);
        MessageInput messageInput = new MessageInput();

        messageScroller.setSizeFull();
        messageInput.setWidthFull();

        messageInput.addSubmitListener(event -> {
            MessageListItem userMessage = new MessageListItem(event.getValue(), Instant.now(), "Me");
            getUI().ifPresent(ui -> ui.access(() -> {
                items.add(userMessage);
                MessageListItem reply = new MessageListItem("", Instant.now(), "DroidComm");
                items.add(reply);
                messageList.setItems(items);
                messages.add(new UserMessage(event.getValue()));

                Flux<String> contentStream = chatClient
                        .prompt()
                        .user(event.getValue())
                        .stream()
                        .content();

                contentStream.subscribe(content -> getUI().ifPresent(ui1 -> ui1.access(() -> {  // Ensure UI modifications happen within the UI thread
                    reply.setText(reply.getText() + content);
                    messageList.setItems(items);
                })),
                        Throwable::printStackTrace,
                        () -> getUI().ifPresent(ui1 -> ui1.access(() -> {
                            messages.add(new AssistantMessage(reply.getText()));
                            messageList.setItems(items);  // Update the message list with the final reply
                        })));

                scrollToBottom(messageScroller);
            }));
        });

        add(messageScroller, messageInput);
    }

    private void scrollToBottom(Scroller scroller) {
        scroller.getElement().executeJs("this.scrollTo(0, this.scrollHeight);");
    }
}
