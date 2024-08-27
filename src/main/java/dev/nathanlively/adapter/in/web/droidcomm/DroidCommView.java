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
import dev.nathanlively.application.AiService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@PageTitle("DroidComm")
@Route(value = "DroidComm", layout = MainLayout.class)
@PermitAll
public class DroidCommView extends VerticalLayout {
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger log = LoggerFactory.getLogger(DroidCommView.class);

    private final List<Message> messages;
    private final List<MessageListItem> items;
    private final MessageList messageList;
    private final AiService aiService;
    private final String chatId;

    public DroidCommView(AiService aiService) {
        this.aiService = aiService;
        this.chatId = String.valueOf(ID_GENERATOR.incrementAndGet());
        this.messages = new ArrayList<>();
        this.items = new ArrayList<>();
        this.messageList = new MessageList(items);

        configureView();
    }

    private void configureView() {
        addClassNames("droid-comm-view", LumoUtility.Width.FULL, LumoUtility.Height.FULL, "display-flex", "flex-auto");
        setSizeFull();

        Scroller messageScroller = createMessageScroller();
        MessageInput messageInput = createMessageInput(messageScroller);

        add(messageScroller, messageInput);
    }

    private Scroller createMessageScroller() {
        Scroller scroller = new Scroller(messageList);
        scroller.setSizeFull();
        return scroller;
    }

    private MessageInput createMessageInput(Scroller messageScroller) {
        MessageInput messageInput = new MessageInput();
        messageInput.setWidthFull();
        messageInput.getElement().executeJs(
                "this.addEventListener('submit', () => {this.setAttribute('timestamp', new Date().toISOString());});");
        messageInput.addSubmitListener(event -> handleMessageSubmit(event, messageScroller));
        return messageInput;
    }

    private void handleMessageSubmit(MessageInput.SubmitEvent event, Scroller messageScroller) {
        String userMessageText = event.getValue();
        String timestamp = event.getSource().getElement().getAttribute("timestamp"); // Read the timestamp attribute
        Instant creationTime = timestamp != null ? Instant.parse(timestamp) : Instant.now(); // Default to now if timestamp isn't set
        log.info("Messaged created at {}", creationTime);
        String userName = "Nathan";
        MessageListItem userMessage = new MessageListItem(userMessageText, creationTime, userName);
        UserMessageDto userMessageDto = new UserMessageDto(creationTime, userName, userMessageText, chatId);
        appendMessageAndReply(userMessage, messageScroller, userMessageText, userMessageDto);
    }

    private void appendMessageAndReply(MessageListItem userMessage, Scroller messageScroller, String userMessageText, UserMessageDto userMessageDto) {
        getUI().ifPresent(ui -> ui.access(() -> {
            addMessagesToUI(userMessage);

            MessageListItem reply = new MessageListItem("", Instant.now(), "DroidComm");
            appendReplyMessages(userMessageText, reply, messageScroller, userMessageDto);
        }));
    }

    private void addMessagesToUI(MessageListItem userMessage) {
        items.add(userMessage);
        messageList.setItems(items);
        messages.add(new UserMessage(userMessage.getText()));
    }

    private void appendReplyMessages(String userMessageText, MessageListItem reply, Scroller messageScroller, UserMessageDto userMessageDto) {
        items.add(reply);
        messageList.setItems(items);

        Flux<String> contentStream = aiService.sendMessageAndReceiveReplies(userMessageDto);

        contentStream.subscribe(
                content -> updateReplyContent(reply, content),
                Throwable::printStackTrace,
                () -> finalizeReply(reply)
        );

        scrollToBottom(messageScroller);
    }

    private void updateReplyContent(MessageListItem reply, String content) {
        getUI().ifPresent(ui1 -> ui1.access(() -> {
            reply.setText(reply.getText() + content);
            messageList.setItems(items);
        }));
    }

    private void finalizeReply(MessageListItem reply) {
        getUI().ifPresent(ui1 -> ui1.access(() -> {
            messages.add(new AssistantMessage(reply.getText()));
            messageList.setItems(items);  // Update the message list with the final reply
        }));
    }

    private void scrollToBottom(Scroller scroller) {
        scroller.getElement().executeJs("this.scrollTo(0, this.scrollHeight);");
    }
}
