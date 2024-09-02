package dev.nathanlively.adapter.in.web.droidcomm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@PageTitle("DroidComm")
@Route(value = "DroidComm", layout = MainLayout.class)
@PermitAll
public class DroidCommView extends VerticalLayout {
    private static final Logger log = LoggerFactory.getLogger(DroidCommView.class);

    private final List<Message> messages;
    private final List<MessageListItem> items;
    private final MessageList messageList;
    private final AiService aiService;
    private final String chatId;

    public DroidCommView(AiService aiService) {
        this.aiService = aiService;
        this.chatId = "1";  // todo: After security is added, update this value to a unique id for the user.
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
        messageInput.addSubmitListener(event -> handleMessageSubmit(event, messageScroller));
        return messageInput;
    }

    private void handleMessageSubmit(MessageInput.SubmitEvent event, Scroller messageScroller) {
        String jsCode = "const now = new Date();"
                + "const timestamp = now.toISOString();"
                + "const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;"
                + "return { timestamp, timezone };";

        event.getSource().getElement().executeJs(jsCode).then(jsonString -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonString.toJson());

                String timestamp = jsonNode.get("timestamp").asText();
                String timezone = jsonNode.get("timezone").asText();

                processSubmission(event.getValue(), timestamp, timezone, messageScroller);
            } catch (Exception e) {
                log.error("Failed to parse timestamp and timezone from JSON: {}", e.getMessage());
            }
        });
    }

    private void processSubmission(String messageText, String timestamp, String timezone, Scroller messageScroller) {
        log.info("Timestamp: {}, Timezone: {}", timestamp, timezone);
        Instant creationTime = Instant.parse(timestamp);
        String userName = "Nathan";
        MessageListItem userMessage = new MessageListItem(messageText, creationTime, userName);

        UserMessageDto userMessageDto = new UserMessageDto(creationTime, userName, messageText, chatId, timezone);
        appendMessageAndReply(userMessage, messageScroller, userMessageDto);

        log.info("Message submitted: {}, Timestamp: {}, Timezone: {}", messageText, timestamp, timezone);
    }

    private void appendMessageAndReply(MessageListItem userMessage, Scroller messageScroller, UserMessageDto userMessageDto) {
        getUI().ifPresent(ui -> ui.access(() -> {
            addMessagesToUI(userMessage);

            MessageListItem reply = new MessageListItem("", Instant.now(), "DroidComm");
            appendReplyMessages(reply, messageScroller, userMessageDto);
        }));
    }

    private void addMessagesToUI(MessageListItem userMessage) {
        items.add(userMessage);
        messageList.setItems(items);
        messages.add(new UserMessage(userMessage.getText()));
    }

    private void appendReplyMessages(MessageListItem reply, Scroller messageScroller, UserMessageDto userMessageDto) {
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
