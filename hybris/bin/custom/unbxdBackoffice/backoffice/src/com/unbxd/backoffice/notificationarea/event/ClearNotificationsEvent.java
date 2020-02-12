package com.unbxd.backoffice.notificationarea.event;

import com.unbxd.backoffice.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.core.events.CockpitEvent;

public class ClearNotificationsEvent implements CockpitEvent {
    public static final String EVENT_NAME = "ClearNotifications";
    private static final String NOT_APPLICABLE = "NA";
    private final Level level;
    private final String source;

    public ClearNotificationsEvent(String source, Level level) {
        this.level = level;
        this.source = source;
    }

    public ClearNotificationsEvent(String source) {
        this(source, (Level)null);
    }

    public String getName() {
        return "ClearNotifications";
    }

    public Object getData() {
        return "NA";
    }

    public String getSource() {
        return this.source;
    }

    public Level getLevel() {
        return this.level;
    }
}
