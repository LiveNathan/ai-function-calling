package dev.nathanlively.adapter.in.web.droidcomm;

import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInput.SubmitEvent;

public class TimestampedSubmitEvent extends SubmitEvent {
    private final String timestamp;
    private final String timezone;

    public TimestampedSubmitEvent(MessageInput source, boolean fromClient,
                                  @EventData("event.detail.value") String value,
                                  @EventData("new Date().toISOString()") String timestamp,
                                  @EventData("Intl.DateTimeFormat().resolvedOptions().timeZone") String timezone) {
        super(source, fromClient, value);
        this.timestamp = timestamp;
        this.timezone = timezone;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTimezone() {
        return timezone;
    }
}
