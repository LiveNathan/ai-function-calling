package dev.nathanlively.adapter.in.web.droidcomm;

import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInput.SubmitEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampedSubmitEvent extends SubmitEvent {
    private final String timestamp;
    private final String timezone;
    private final LocalDateTime localDateTime;

    public TimestampedSubmitEvent(MessageInput source, boolean fromClient,
                                  @EventData("event.detail.value") String value,
                                  @EventData("new Date().toISOString()") String timestamp,
                                  @EventData("Intl.DateTimeFormat().resolvedOptions().timeZone") String timezone) {
        super(source, fromClient, value);
        this.timestamp = timestamp;
        this.timezone = timezone;
        this.localDateTime = timestamp != null ? LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME) : LocalDateTime.now();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTimezone() {
        return timezone;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
